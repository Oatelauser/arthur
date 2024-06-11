package com.arthur.boot.file.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Excel文件上传请求参数标识注解，如果不知道sheet，默认会读所有的sheet
 *
 * @author DearYang
 * @date 2022-09-28
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
public @interface ExcelParam {

	String DEFAULT_PARAMETER_NAME = "file";

	/**
	 * Excel文件上传的请求参数名
	 *
	 * @return 请求参数名
	 */
	@AliasFor("name")
	String value() default DEFAULT_PARAMETER_NAME;

	/**
	 * @see ExcelParam#value()
	 */
	@AliasFor("value")
	String name() default DEFAULT_PARAMETER_NAME;

	/**
	 * Excel的密码
	 */
	String password() default "";

	/**
	 * excel sheet名称，和{@link #sheetNo()}互斥
	 *
	 * @return sheet名称
	 */
	String[] sheetName() default {};

	/**
	 * Sheet 下标，和{@link #sheetName()}互斥，优先使用{@link #sheetName()}
	 */
	int[] sheetNo() default {};

	/**
	 * 表头所在行的位置
	 *
	 * @see com.alibaba.excel.read.builder.AbstractExcelReaderParameterBuilder#headRowNumber(Integer)
	 */
	int headRowNumber() default 1;

	/**
	 * 请求参数是否必须存在请求中
	 * <p>
	 * 默认为true，如果请求中缺少该请求参数，则会引发异常
	 * 如果在请求中不存在该部件，您希望该部件为空值，则将该值切换为false
	 */
	boolean required() default true;

}
