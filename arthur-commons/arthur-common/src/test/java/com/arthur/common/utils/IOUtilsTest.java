package com.arthur.common.utils;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class IOUtilsTest {

	@Test
	void getChars() {
		byte[] bytes = new byte[4];
		IOUtils.writeToByteArray(2, 1, bytes);
		IOUtils.writeToByteArray(0, 2, bytes);
		IOUtils.writeToByteArray(2, 3, bytes);
		IOUtils.writeToByteArray(3, 4, bytes);
		System.out.println(FastString.STRING_CREATOR.apply(bytes, StandardCharsets.ISO_8859_1));
	}

	@Test
	void testGetChars() {
	}

	@Test
	void testGetChars1() {
	}

	@Test
	void testGetChars2() {
	}

	@Test
	void encodeUTF8() {
	}

	@Test
	void testEncodeUTF8() {
	}

	@Test
	void decodeUTF8() {
	}

	@Test
	void testDecodeUTF8() {
	}
}
