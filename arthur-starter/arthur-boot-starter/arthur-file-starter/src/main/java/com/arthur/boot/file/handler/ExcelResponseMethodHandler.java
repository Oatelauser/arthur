package com.arthur.boot.file.handler;

import com.arthur.boot.file.annotation.ExcelResponse;
import com.arthur.boot.file.excel.ExcelDataEntity;
import com.arthur.boot.file.excel.HttpExcelResponse;
import com.arthur.boot.utils.AnnotationUtils;
import com.arthur.web.model.ServerResponse;
import com.arthur.web.servlet.server.WebServiceResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.arthur.boot.file.constants.ServerStatusEnum.EMPTY_DATA_FILE;
import static com.arthur.web.model.ServerResponse.ofError;

/**
 * 处理控制器注解{@link ExcelResponse}的执行器
 *
 * @author DearYang
 * @date 2022-09-28
 * @since 1.0
 */
public class ExcelResponseMethodHandler implements HandlerMethodReturnValueHandler {

    private final WebServiceResponse webServiceResponse;

    public ExcelResponseMethodHandler(WebServiceResponse webServiceResponse) {
        this.webServiceResponse = webServiceResponse;
    }

    @Override
    public boolean supportsReturnType(@NonNull MethodParameter returnType) {
        if (AnnotationUtils.findMethodAnnotation(ExcelResponse.class, returnType) == null) {
            return false;
        }
        Class<?> parameterType = returnType.getParameterType();
        if (Collection.class.isAssignableFrom(parameterType)) {
            return true;
        }
        return ExcelDataEntity.class.isAssignableFrom(parameterType);
    }

    @Override
    public void handleReturnValue(Object returnValue, @NonNull MethodParameter returnType,
            ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);

        ExcelResponse annotation = AnnotationUtils.findMethodAnnotation(ExcelResponse.class, returnType);
        Assert.notNull(annotation, "Not annotation @ExcelResponse");
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Assert.notNull(response, "Not HttpServletResponse");

        HandlerReturnEntity returnEntity = this.resolveReturnTypeAndValue(returnValue, returnType);
        Collection<?> returnValues = returnEntity.returnValues;
        if (CollectionUtils.isEmpty(returnValues)) {
            if (!annotation.allowEmptyFile()) {
                this.handleEmptyFile(response);
                return;
            }

            returnValues = List.of();
        }

        // 设置响应信息
        try (HttpExcelResponse excelResponse = new HttpExcelResponse(response)) {
			// 响应中写入excel数据
            excelResponse.setFileName(annotation.fileName());
            excelResponse.write(annotation.sheetName(), returnEntity.dataType, returnValues);
        }
    }

    private HandlerReturnEntity resolveReturnTypeAndValue(Object returnValue, MethodParameter returnType) {
        if (returnValue instanceof ExcelDataEntity<?> dataResponse) {
            return new HandlerReturnEntity(dataResponse.dataType(), dataResponse.data());
        }

        Collection<?> returnValues = (Collection<?>) returnValue;
        Class<?> resolveParameterType = CollectionUtils.findCommonElementType(returnValues);
        if (resolveParameterType == null) {
            resolveParameterType = ResolvableType.forMethodParameter(returnType).asCollection()
                    .getGeneric(0).resolve();
        }
        return new HandlerReturnEntity(resolveParameterType, returnValues);
    }

    protected void handleEmptyFile(HttpServletResponse response) {
        try {
            ServerResponse serverResponse = ofError(EMPTY_DATA_FILE);
            webServiceResponse.jsonWriteTo(serverResponse, response);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private record HandlerReturnEntity(Class<?> dataType, Collection<?> returnValues) {
    }


}
