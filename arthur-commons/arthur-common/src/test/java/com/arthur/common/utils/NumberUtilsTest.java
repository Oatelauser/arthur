package com.arthur.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberUtilsTest {

	@Test
	void abs() {
		assertEquals(131, NumberUtils.abs(-131));
		assertEquals(13, NumberUtils.abs(13));
	}

}
