package com.arthur.lang.utils;

import com.arthur.lang.FastString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

class IOUtilsTest {

	@Test
	void encodeUTF8() {
		String STR = "01234567890ABCDEFGHIJKLMNOPQRSTUVWZYZabcdefghijklmnopqrstuvwzyz一二三四五六七八九十";
		byte[] out;
		out = new byte[STR.length() * 3];
		long valueFieldOffset = 0;
		try {
			Field valueField = String.class.getDeclaredField("value");
			valueFieldOffset = UnsafeUtils.objectFieldOffset(valueField);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		byte[] chars = UnsafeUtils.getObject(STR, valueFieldOffset);
		int len = IOUtils.encodeUTF8(chars, 0, chars.length, out, 0);
		byte[] bytes = Arrays.copyOf(out, len);
		int a = 1;
	}

	@Test
	void decodeUTF8() {
		byte[] utf8Bytes = "01234567890ABCDEFGHIJKLMNOPQRSTUVWZYZabcdefghijklmnopqrstuvwzyz"
			.getBytes(StandardCharsets.UTF_8);
		byte[] buf = new byte[utf8Bytes.length * 2];
		int len = IOUtils.decodeUTF8(utf8Bytes, 0, utf8Bytes.length, buf);
		byte[] chars = Arrays.copyOf(buf, len);
		FastString.STRING_CREATOR.apply(chars, StandardCharsets.US_ASCII);
	}

	@Test
	void writeCharToByte() {
		String s = "一二三";
		byte[] bytes = new byte[50];
		int len = IOUtils.writeCharToByte(s.toCharArray(), bytes);
		byte[] bytes1 = Arrays.copyOf(bytes, len);
		Assertions.assertEquals(s, FastString.STRING_CONSTRUCTOR.apply(bytes1, FastString.UTF16));
	}

}
