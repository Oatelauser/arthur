package com.arthur.boot.file.excel;

import com.arthur.boot.file.excel.ExcelRows.ExcelRow;
import org.springframework.core.convert.converter.Converter;

/**
 * Spring的类型转换器：{@link ExcelRow} -> {@link Object}
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-28
 * @since 1.0
 */
public class ExcelRowConverter implements Converter<ExcelRow<?>, Object> {

	@Override
	public Object convert(ExcelRow<?> source) {
		return source.data();
	}
	
}
