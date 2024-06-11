package com.arthur.boot.file.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

import static com.arthur.boot.file.constants.FileConstants.DEFAULT_EXCEL_FILE_NAME;
import static com.arthur.boot.file.constants.FileConstants.DEFAULT_SHEET_NAME;

/**
 * Excel文件下载响应注解标识
 *
 * @author DearYang
 * @date 2022-09-28
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface ExcelResponse {

    /**
     * 文件名
     */
    @AliasFor("fileName")
    String value() default DEFAULT_EXCEL_FILE_NAME;

    /**
     * 文件名
     */
    @AliasFor("value")
    String fileName() default DEFAULT_EXCEL_FILE_NAME;

    /**
     * sheet名称
     */
    String sheetName() default DEFAULT_SHEET_NAME;

    /**
     * 是否允许空数据生成文件
     * <p>
     * 如果不允许，在会产生错误响应
     */
    boolean allowEmptyFile() default true;

}
