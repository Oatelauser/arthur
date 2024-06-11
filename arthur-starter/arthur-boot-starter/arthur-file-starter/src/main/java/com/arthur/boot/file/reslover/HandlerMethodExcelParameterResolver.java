package com.arthur.boot.file.reslover;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.arthur.boot.file.annotation.ExcelParam;
import com.arthur.boot.file.excel.ExcelRows;
import com.arthur.boot.file.exception.UnsupportedTypeConvertException;
import com.arthur.boot.utils.AnnotationUtils;
import com.arthur.web.servlet.utils.WebMvcUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.arthur.boot.file.utils.HandlerMethodValidation.validateParameterIfApplicable;
import static org.springframework.web.multipart.support.MultipartResolutionDelegate.isMultipartRequest;
import static org.springframework.web.multipart.support.MultipartResolutionDelegate.resolveMultipartRequest;

/**
 * 处理控制器方法参数中的{@link ExcelParam}注解进行文件上传
 *
 * @author DearYang
 * @date 2022-09-29
 * @since 1.0
 */
public class HandlerMethodExcelParameterResolver implements HandlerMethodArgumentResolver {
	private final ConversionService conversionService;
	private final TypeDescriptor sourceType = TypeDescriptor.collection(List.class,
		TypeDescriptor.valueOf(ExcelRows.ExcelRow.class));

	public HandlerMethodExcelParameterResolver(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public boolean supportsParameter(@NonNull MethodParameter parameter) {
		if (AnnotationUtils.getMethodParameterAnnotation(ExcelParam.class, parameter) == null) {
			return false;
		}

		// 判断请求是否文件上传请求
		Optional<HttpServletRequest> optional = WebMvcUtils.getRequest();
		if (optional.isEmpty()) {
			return false;
		}
		if (!isMultipartRequest(optional.get())) {
			return false;
		}
		// 判断支持的参数类型
		Class<?> parameterClass = parameter.getParameterType();
		return Collection.class.isAssignableFrom(parameterClass) || ExcelRows.class.isAssignableFrom(parameterClass);
	}

	@Override
	public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
			@NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		ExcelParam annotation = AnnotationUtils.getMethodParameterAnnotation(ExcelParam.class, parameter);
		Assert.notNull(annotation, "Handler method not @ExcelParam");

		// 获取请求参数名
		String name = annotation.name();
		Assert.state(StringUtils.hasText(name), "Resolve parameter name fail");

		// 解析MultipartRequest请求
		MultipartRequest multipartRequest = resolveMultipartRequest(webRequest);
		if (multipartRequest == null) {
			if (annotation.required()) {
				throw new MissingServletRequestPartException(name);
			} else {
				return null;
			}
		}
		List<MultipartFile> files = multipartRequest.getFiles(name);
		Assert.notEmpty(files, "Not MultipartFile");

		// 读取文件写入对象
		ExcelRows<Object> excelRows = new ExcelRows<>();
		for (MultipartFile file : files) {
			this.readExcelFile(annotation, parameter, file, excelRows);
		}
		// Validate 验证
		validateParameterIfApplicable(excelRows, parameter, mavContainer, webRequest, binderFactory);

		// 类型转换
		if (ExcelRows.class.isAssignableFrom(parameter.getParameterType())) {
			return excelRows;
		}
		return this.adaptExcelRows(excelRows, parameter);
	}

	private Object adaptExcelRows(ExcelRows<Object> excelRows, MethodParameter parameter) {
		TypeDescriptor targetType = TypeDescriptor.collection(parameter.getParameterType(),
			TypeDescriptor.valueOf(Object.class));
		if (!conversionService.canConvert(sourceType, targetType)) {
			throw new UnsupportedTypeConvertException("Cannot convert object: sourceType ["
				+ sourceType + "], TargetType [" + targetType + "]");
		}

		return conversionService.convert(excelRows.getRows(), sourceType, targetType);
	}

	private void readExcelFile(ExcelParam annotation, MethodParameter parameter, MultipartFile file,
			ExcelRows<Object> excelRows) throws IOException {
		Class<?> paramGenericType = resolveParameterGenericType(parameter);
		ExcelReaderBuilder readerBuilder = EasyExcel.read(file.getInputStream(), paramGenericType, new AnalysisEventListener<>() {
			@Override
			public void invoke(Object data, AnalysisContext context) {
				excelRows.addExcelRow(context.readRowHolder().getRowIndex(), data);
			}

			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {
				excelRows.setHeader(context.currentReadHolder().excelReadHeadProperty());
			}
		});
		if (!AnnotationUtils.isDefaultValue(annotation, "password")) {
			readerBuilder.password(annotation.password());
		}

		readerBuilder.headRowNumber(annotation.headRowNumber());
		// 读指定的sheetName
		String[] sheetNames = annotation.sheetName();
		if (!ObjectUtils.isEmpty(sheetNames)) {
			for (String sheetName : sheetNames) {
				readerBuilder.sheet(sheetName).doRead();
			}
			return;
		}
		// 读指定的sheetNo
		int[] sheetNos = annotation.sheetNo();
		if (!ObjectUtils.isEmpty(sheetNos)) {
			for (int sheetNo : sheetNos) {
				readerBuilder.sheet(sheetNo).doRead();
			}
			return;
		}
		// 读所有的sheet
		readerBuilder.doReadAll();
	}

	/**
	 * 解析控制器方法参数的泛型类型
	 *
	 * @param parameter 方法参数
	 * @return 泛型类型
	 */
	public static Class<?> resolveParameterGenericType(MethodParameter parameter) {
		Class<?> parameterType = parameter.getParameterType();
		ResolvableType resolveType = ResolvableType.forMethodParameter(parameter);

		// 类型转换
		if (ExcelRows.class.isAssignableFrom(parameterType)) {
			resolveType = resolveType.as(ExcelRows.class);
		} else if (Collection.class.isAssignableFrom(parameterType)) {
			resolveType = resolveType.asCollection();
		}

		return resolveType.resolveGeneric(0);
	}

}
