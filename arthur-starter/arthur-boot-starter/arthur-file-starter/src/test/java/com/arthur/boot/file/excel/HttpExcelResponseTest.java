package com.arthur.boot.file.excel;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class HttpExcelResponseTest {

	void t(String sheetName, Collection<?> data) {
		int size = data.size();
		int step = 3;
		if (size < step) {
			System.out.println(sheetName);
			return;
		}

		int i = 0;
		int start = 0;
		List<?> rows = data instanceof List<?> ? (List<?>) data : new ArrayList<>(data);
		for (int end = step; end < size; i++, start += step, end += step) {
			System.out.println(sheetName + i);
		}
		if (size - start > 0) {
			System.out.println(sheetName + i);
		}
	}

	@Test
	public void t1() {
		t("sheet", List.of(1, 2, 3, 4, 5));
	}

}
