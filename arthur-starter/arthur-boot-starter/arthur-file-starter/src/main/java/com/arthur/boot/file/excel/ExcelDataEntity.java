package com.arthur.boot.file.excel;

import java.util.Collection;

/**
 * Excel数据下载对象（设置数据类型、数据集合），用于支持{@link com.arthur.boot.file.annotation.ExcelResponse}注解
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-21
 * @since 1.0
 */
public record ExcelDataEntity<T>(Class<T> dataType, Collection<T> data) {
}
