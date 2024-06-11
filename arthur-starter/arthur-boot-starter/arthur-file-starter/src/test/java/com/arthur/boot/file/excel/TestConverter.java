package com.arthur.boot.file.excel;

import org.junit.jupiter.api.Test;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.List;
import java.util.Set;

/**
 * 概要描述
 * <p>
 * 详细描述
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-28
 * @since 1.0
 */
public class TestConverter {

	@Test
	public void t() {
		DefaultConversionService conversionService = new DefaultConversionService();
		Converter<ExcelRows.ExcelRow<?>, Object> converter = new Converter<>() {
			@Override
			public Object convert(ExcelRows.ExcelRow<?> source) {
				return source.data();
			}
		};
		conversionService.addConverter(converter);

		ExcelRows<String> excelRows = new ExcelRows<>();
		ExcelRows.ExcelRow<String> row1 = new ExcelRows.ExcelRow<>(0, "0");
		ExcelRows.ExcelRow<String> row2 = new ExcelRows.ExcelRow<>(1, "1");
		ExcelRows.ExcelRow<String> row3 = new ExcelRows.ExcelRow<>(2, "2");
		excelRows.setRows(List.of(row1, row2, row3));

		Object convert = conversionService.convert(excelRows.getRows(), TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(ExcelRows.ExcelRow.class)),
			TypeDescriptor.collection(Set.class, TypeDescriptor.valueOf(Object.class)));
		int a = 1;
	}

}
