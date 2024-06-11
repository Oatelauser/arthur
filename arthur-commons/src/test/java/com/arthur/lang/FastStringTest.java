package com.arthur.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FastStringTest {

	@Test
	void encodeUTF8() {
		String a = "六六六六";
		Assertions.assertEquals(a, FastString.encodeUTF8(a));
	}

	@Test
	void decodeUTF8() {
	}

}
