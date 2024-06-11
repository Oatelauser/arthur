package com.arthur.boot.file.excel;

import com.alibaba.excel.read.metadata.property.ExcelReadHeadProperty;
import jakarta.validation.Valid;
import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.ValueExtractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel读取信息，包括每行数据和元数据信息
 *
 * @author DearYang
 * @date 2022-09-28
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ExcelRows<T> {

	/**
	 * excel头元数据
	 */
	private ExcelReadHeadProperty header;

	/**
	 * 行数据
	 */
	@Valid
	private List<ExcelRow<T>> rows = new ArrayList<>();

	public ExcelReadHeadProperty getHeader() {
		return header;
	}

	public void setHeader(ExcelReadHeadProperty header) {
		this.header = header;
	}

	public List<ExcelRow<T>> getRows() {
		return rows;
	}

	public void setRows(List<ExcelRow<T>> rows) {
		this.rows = rows;
	}

	public void addExcelRow(Integer index, T data) {
		rows.add(new ExcelRow<>(index, data));
	}

	/**
	 * @param index 行下标，从0开始
	 * @param data  行数据
	 */
	public record ExcelRow<T>(Integer index, @Valid T data) {
	}

	public static class ExcelRowsContainer {

		public ExcelRows<@Valid ?> excelRows;

	}

	public static class Extractor implements ValueExtractor<ExcelRow<@ExtractedValue ?>> {

		@Override
		public void extractValues(ExcelRow<?> originalValue, ValueReceiver receiver) {
			receiver.value(null, originalValue.data);
		}
	}

}
