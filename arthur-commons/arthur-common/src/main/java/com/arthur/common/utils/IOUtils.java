package com.arthur.common.utils;

import java.util.Arrays;

/**
 * IO工具类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-27
 * @since 1.0
 */
@SuppressWarnings("DuplicatedCode")
public class IOUtils {

	public static final byte[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
		'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
	};

	public static final byte[] DigitTens = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1',
		'1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3',
		'3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5',
		'5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7',
		'7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9'
	};

	public static final byte[] DigitOnes = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
		'9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
	};

	/**
	 * 写数字到字节数组
	 *
	 * @param i     写入字节数组的数字
	 * @param index 写入数组的下标，下标从1开始
	 * @param buf   数组
	 */
	public static void writeToByteArray(int i, int index, byte[] buf) {
		int q, r, p = index;
		byte sign = 0;

		if (i < 0) {
			sign = '-';
			i = -i;
		}

		while (i >= 65536) {
			q = i / 100;
			// really: r = i - (q * 100);
			r = i - ((q << 6) + (q << 5) + (q << 2));
			i = q;
			buf[--p] = DigitOnes[r];
			buf[--p] = DigitTens[r];
		}

		// Fall thru to fast mode for smaller numbers
		// assert(i <= 65536, i);
		do {
			q = (i * 52429) >>> (16 + 3);
			r = i - ((q << 3) + (q << 1)); // r = i-(q*10) ...
			buf[--p] = digits[r];
			i = q;
		} while (i != 0);
		if (sign != 0) {
			buf[--p] = sign;
		}
	}

	/**
	 * 写数字到字节数组
	 *
	 * @param i     写入字节数组的数字
	 * @param index 写入数组的下标，下标从1开始
	 * @param buf   数组
	 */
	public static void writeToByteArray(long i, int index, byte[] buf) {
		long q;
		int r;
		int charPos = index;
		byte sign = 0;

		if (i < 0) {
			sign = '-';
			i = -i;
		}

		// Get 2 digits/iteration using longs until quotient fits into an int
		while (i > Integer.MAX_VALUE) {
			q = i / 100;
			// really: r = i - (q * 100);
			r = (int) (i - ((q << 6) + (q << 5) + (q << 2)));
			i = q;
			buf[--charPos] = DigitOnes[r];
			buf[--charPos] = DigitTens[r];
		}

		// Get 2 digits/iteration using ints
		int q2;
		int i2 = (int) i;
		while (i2 >= 65536) {
			q2 = i2 / 100;
			// really: r = i2 - (q * 100);
			r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
			i2 = q2;
			buf[--charPos] = DigitOnes[r];
			buf[--charPos] = DigitTens[r];
		}

		// Fall thru to fast mode for smaller numbers
		// assert(i2 <= 65536, i2);
		do {
			q2 = (i2 * 52429) >>> (16 + 3);
			r = i2 - ((q2 << 3) + (q2 << 1)); // r = i2-(q2*10) ...
			buf[--charPos] = digits[r];
			i2 = q2;
		} while (i2 != 0);
		if (sign != 0) {
			buf[--charPos] = sign;
		}
	}

	/**
	 * 写数字到字符数组
	 *
	 * @param i     写入字符数组的数字
	 * @param index 写入数组的下标，下标从1开始
	 * @param buf   数组
	 */
	public static void writeToCharArray(int i, int index, char[] buf) {
		int q, r, p = index;
		char sign = 0;

		if (i < 0) {
			sign = '-';
			i = -i;
		}

		while (i >= 65536) {
			q = i / 100;
			// really: r = i - (q * 100);
			r = i - ((q << 6) + (q << 5) + (q << 2));
			i = q;
			buf[--p] = (char) DigitOnes[r];
			buf[--p] = (char) DigitTens[r];
		}

		// Fall thru to fast mode for smaller numbers
		// assert(i <= 65536, i);
		do {
			q = (i * 52429) >>> (16 + 3);
			r = i - ((q << 3) + (q << 1)); // r = i-(q*10) ...
			buf[--p] = (char) digits[r];
			i = q;
		} while (i != 0);
		if (sign != 0) {
			buf[--p] = sign;
		}
	}

	/**
	 * 写数字到字符数组
	 *
	 * @param i     写入字符数组的数字
	 * @param index 写入数组的下标，下标从1开始
	 * @param buf   数组
	 */
	public static void writeToCharArray(long i, int index, char[] buf) {
		long q;
		int r;
		int charPos = index;
		char sign = 0;

		if (i < 0) {
			sign = '-';
			i = -i;
		}

		// Get 2 digits/iteration using longs until quotient fits into an int
		while (i > Integer.MAX_VALUE) {
			q = i / 100;
			// really: r = i - (q * 100);
			r = (int) (i - ((q << 6) + (q << 5) + (q << 2)));
			i = q;
			buf[--charPos] = (char) DigitOnes[r];
			buf[--charPos] = (char) DigitTens[r];
		}

		// Get 2 digits/iteration using ints
		int q2;
		int i2 = (int) i;
		while (i2 >= 65536) {
			q2 = i2 / 100;
			// really: r = i2 - (q * 100);
			r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
			i2 = q2;
			buf[--charPos] = (char) DigitOnes[r];
			buf[--charPos] = (char) DigitTens[r];
		}

		// Fall thru to fast mode for smaller numbers
		// assert(i2 <= 65536, i2);
		do {
			q2 = (i2 * 52429) >>> (16 + 3);
			r = i2 - ((q2 << 3) + (q2 << 1)); // r = i2-(q2*10) ...
			buf[--charPos] = (char) digits[r];
			i2 = q2;
		} while (i2 != 0);
		if (sign != 0) {
			buf[--charPos] = sign;
		}
	}

	/**
	 * 扩容数组
	 *
	 * @param minCapacity 最小容量
	 * @param bytes       数组
	 * @return 扩容后的数组
	 */
	public static byte[] ensureCapacity(int minCapacity, byte[] bytes) {
		if (minCapacity > bytes.length) {
			int oldCapacity = bytes.length;
			int newCapacity = oldCapacity + (oldCapacity >> 1);
			if (newCapacity < minCapacity) {
				newCapacity = minCapacity;
			}
			//if (newCapacity - maxArraySize > 0) {
			//	throw new OutOfMemoryError();
			//}

			// minCapacity is usually close to size, so this is a win:
			bytes = Arrays.copyOf(bytes, newCapacity);
		}
		return bytes;
	}

	/**
	 * 编码UTF8字节数组
	 *
	 * @param src    字节数组
	 * @param offset 字节数组开始位置
	 * @param len    从开始位置后需要编码的字节长度
	 * @param dst    编码的字节数组设置到指定的数组里面
	 * @param dp     指定数组的开始位置
	 * @return 编码后的字节数组占用长度
	 */
	@SuppressWarnings("all")
	public static int encodeUTF8(byte[] src, int offset, int len, byte[] dst, int dp) {
		int sl = offset + len;
		while (offset < sl) {
			byte b0 = src[offset++];
			byte b1 = src[offset++];

			if (b1 == 0 && b0 >= 0) {
				dst[dp++] = b0;
			} else {
				char c = (char) (((b0 & 0xff) << 0) | ((b1 & 0xff) << 8));
				if (c < 0x800) {
					// 2 bytes, 11 bits
					dst[dp++] = (byte) (0xc0 | (c >> 6));
					dst[dp++] = (byte) (0x80 | (c & 0x3f));
				} else if (c >= '\uD800' && c < ('\uDFFF' + 1)) { //Character.isSurrogate(c) but 1.7
					final int uc;
					int ip = offset - 1;
					if (c >= '\uD800' && c < ('\uDBFF' + 1)) { // Character.isHighSurrogate(c)
						if (sl - ip < 2) {
							uc = -1;
						} else {
							b0 = src[ip + 1];
							b1 = src[ip + 2];
							char d = (char) (((b0 & 0xff) << 0) | ((b1 & 0xff) << 8));
							// d >= '\uDC00' && d < ('\uDFFF' + 1)
							if (d >= '\uDC00' && d < ('\uDFFF' + 1)) { // Character.isLowSurrogate(d)
								offset += 2;
								uc = ((c << 10) + d) + (0x010000 - ('\uD800' << 10) - '\uDC00'); // Character.toCodePoint(c, d)
							} else {
								return -1;
							}
						}
					} else {
						//
						if (c >= '\uDC00' && c < ('\uDFFF' + 1)) { // Character.isLowSurrogate(c)
							return -1;
						} else {
							uc = c;
						}
					}

					if (uc < 0) {
						dst[dp++] = (byte) '?';
					} else {
						dst[dp++] = (byte) (0xf0 | ((uc >> 18)));
						dst[dp++] = (byte) (0x80 | ((uc >> 12) & 0x3f));
						dst[dp++] = (byte) (0x80 | ((uc >> 6) & 0x3f));
						dst[dp++] = (byte) (0x80 | (uc & 0x3f));
					}
				} else {
					// 3 bytes, 16 bits
					dst[dp++] = (byte) (0xe0 | ((c >> 12)));
					dst[dp++] = (byte) (0x80 | ((c >> 6) & 0x3f));
					dst[dp++] = (byte) (0x80 | (c & 0x3f));
				}
			}
		}
		return dp;
	}

	/**
	 * 编码UTF8字节数组
	 *
	 * @param src    字符数组
	 * @param offset 字符数组开始位置
	 * @param len    从开始位置后需要编码的字符长度
	 * @param dst    编码的字符数组设置到指定的数组里面
	 * @param dp     指定数组的开始位置
	 * @return 编码后的字符数组占用长度
	 */
	@SuppressWarnings("all")
	public static int encodeUTF8(char[] src, int offset, int len, byte[] dst, int dp) {
		int sl = offset + len;
		int dlASCII = dp + Math.min(len, dst.length);

		// ASCII only optimized loop
		while (dp < dlASCII && src[offset] < '\u0080') {
			dst[dp++] = (byte) src[offset++];
		}

		while (offset < sl) {
			char c = src[offset++];
			if (c < 0x80) {
				// Have at most seven bits
				dst[dp++] = (byte) c;
			} else if (c < 0x800) {
				// 2 bytes, 11 bits
				dst[dp++] = (byte) (0xc0 | (c >> 6));
				dst[dp++] = (byte) (0x80 | (c & 0x3f));
			} else if (c >= '\uD800' && c < ('\uDFFF' + 1)) { //Character.isSurrogate(c) but 1.7
				final int uc;
				int ip = offset - 1;
				if (c >= '\uD800' && c < ('\uDBFF' + 1)) { // Character.isHighSurrogate(c)
					if (sl - ip < 2) {
						uc = -1;
					} else {
						char d = src[ip + 1];
						// d >= '\uDC00' && d < ('\uDFFF' + 1)
						if (d >= '\uDC00' && d < ('\uDFFF' + 1)) { // Character.isLowSurrogate(d)
							uc = ((c << 10) + d) + (0x010000 - ('\uD800' << 10) - '\uDC00'); // Character.toCodePoint(c, d)
						} else {
//                            throw new JSONException("encodeUTF8 error", new MalformedInputException(1));
							dst[dp++] = (byte) '?';
							continue;
						}
					}
				} else {
					//
					if (c >= '\uDC00' && c < ('\uDFFF' + 1)) { // Character.isLowSurrogate(c)
						dst[dp++] = (byte) '?';
						continue;
//                        throw new JSONException("encodeUTF8 error", new MalformedInputException(1));
					} else {
						uc = c;
					}
				}

				if (uc < 0) {
					dst[dp++] = (byte) '?';
				} else {
					dst[dp++] = (byte) (0xf0 | ((uc >> 18)));
					dst[dp++] = (byte) (0x80 | ((uc >> 12) & 0x3f));
					dst[dp++] = (byte) (0x80 | ((uc >> 6) & 0x3f));
					dst[dp++] = (byte) (0x80 | (uc & 0x3f));
					offset++; // 2 chars
				}
			} else {
				// 3 bytes, 16 bits
				dst[dp++] = (byte) (0xe0 | ((c >> 12)));
				dst[dp++] = (byte) (0x80 | ((c >> 6) & 0x3f));
				dst[dp++] = (byte) (0x80 | (c & 0x3f));
			}
		}
		return dp;
	}

	/**
	 * 解码UTF8字节数组
	 *
	 * @param src 字节数组
	 * @param off 字节数组开始位置
	 * @param len 从开始位置后需要编码的字节长度
	 * @param dst 解码的字节数组设置到指定的数组里面
	 * @return 解码后的字节数组占用长度
	 */
	@SuppressWarnings("all")
	public static int decodeUTF8(byte[] src, int off, int len, byte[] dst) {
		final int sl = off + len;
		int dp = 0;

		while (off < sl) {
			int b0 = src[off++];
			if (b0 >= 0) {
				// 1 byte, 7 bits: 0xxxxxxx
				dst[dp++] = (byte) b0;
				dst[dp++] = 0;
			} else if ((b0 >> 5) == -2 && (b0 & 0x1e) != 0) {
				// 2 bytes, 11 bits: 110xxxxx 10xxxxxx
				if (off < sl) {
					int b1 = src[off++];
					if ((b1 & 0xc0) != 0x80) { // isNotContinuation(b2)
						return -1;
					} else {
						char c = (char) (((b0 << 6) ^ b1) ^
							(((byte) 0xC0 << 6) ^
								((byte) 0x80 << 0)));
						dst[dp++] = (byte) c;
						dst[dp++] = (byte) (c >> 8);
					}
					continue;
				}
				dst[dp++] = (byte) b0;
				dst[dp++] = 0;
				break;
			} else if ((b0 >> 4) == -2) {
				// 3 bytes, 16 bits: 1110xxxx 10xxxxxx 10xxxxxx
				if (off + 1 < sl) {
					int b1 = src[off++];
					int b2 = src[off++];
					if ((b0 == (byte) 0xe0 && (b1 & 0xe0) == 0x80) //
						|| (b1 & 0xc0) != 0x80 //
						|| (b2 & 0xc0) != 0x80) { // isMalformed3(b0, b1, b2)
						return -1;
					} else {
						char c = (char)
							((b0 << 12) ^
								(b1 << 6) ^
								(b2 ^ (((byte) 0xE0 << 12) ^
									((byte) 0x80 << 6) ^
									((byte) 0x80 << 0)))
							);
						boolean isSurrogate = c >= '\uD800' && c < ('\uDFFF' + 1);
						if (isSurrogate) {
							return -1;
						} else {
							dst[dp++] = (byte) c;
							dst[dp++] = (byte) (c >> 8);
						}
					}
					continue;
				}
				return -1;
			} else if ((b0 >> 3) == -2) {
				// 4 bytes, 21 bits: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
				if (off + 2 < sl) {
					int b2 = src[off++];
					int b3 = src[off++];
					int b4 = src[off++];
					int uc = ((b0 << 18) ^
						(b2 << 12) ^
						(b3 << 6) ^
						(b4 ^
							(((byte) 0xF0 << 18) ^
								((byte) 0x80 << 12) ^
								((byte) 0x80 << 6) ^
								((byte) 0x80 << 0))));
					if (((b2 & 0xc0) != 0x80 || (b3 & 0xc0) != 0x80 || (b4 & 0xc0) != 0x80) // isMalformed4
						||
						// shortest form check
						!(uc >= 0x010000 && uc < 0X10FFFF + 1) // !Character.isSupplementaryCodePoint(uc)
					) {
						return -1;
					} else {
						char c = (char) ((uc >>> 10) + ('\uD800' - (0x010000 >>> 10)));
						dst[dp++] = (byte) c;
						dst[dp++] = (byte) (c >> 8);

						c = (char) ((uc & 0x3ff) + '\uDC00');
						dst[dp++] = (byte) c;
						dst[dp++] = (byte) (c >> 8);
					}
					continue;
				}
				return -1;
			} else {
				return -1;
			}
		}
		return dp;
	}

	/**
	 * 解码UTF8字符数组
	 *
	 * @param src 字符数组
	 * @param off 字符数组开始位置
	 * @param len 从开始位置后需要编码的字符长度
	 * @param dst 解码的字符数组设置到指定的数组里面
	 * @return 解码后的字符数组占用长度
	 */
	@SuppressWarnings("all")
	public static int decodeUTF8(byte[] src, int off, int len, char[] dst) {
		final int sl = off + len;
		int dp = 0;
		int dlASCII = Math.min(len, dst.length);

		// ASCII only optimized loop
		while (dp < dlASCII && src[off] >= 0) {
			dst[dp++] = (char) src[off++];
		}

		while (off < sl) {
			int b1 = src[off++];
			if (b1 >= 0) {
				// 1 byte, 7 bits: 0xxxxxxx
				dst[dp++] = (char) b1;
			} else if ((b1 >> 5) == -2 && (b1 & 0x1e) != 0) {
				// 2 bytes, 11 bits: 110xxxxx 10xxxxxx
				if (off < sl) {
					int b2 = src[off++];
					if ((b2 & 0xc0) != 0x80) { // isNotContinuation(b2)
						return -1;
					} else {
						dst[dp++] = (char) (((b1 << 6) ^ b2) ^
							(((byte) 0xC0 << 6) ^
								((byte) 0x80 << 0)));
					}
					continue;
				}
				return -1;
			} else if ((b1 >> 4) == -2) {
				// 3 bytes, 16 bits: 1110xxxx 10xxxxxx 10xxxxxx
				if (off + 1 < sl) {
					int b2 = src[off++];
					int b3 = src[off++];
					if ((b1 == (byte) 0xe0 && (b2 & 0xe0) == 0x80) //
						|| (b2 & 0xc0) != 0x80 //
						|| (b3 & 0xc0) != 0x80) { // isMalformed3(b1, b2, b3)
						return -1;
					} else {
						char c = (char) ((b1 << 12) ^
							(b2 << 6) ^
							(b3 ^
								(((byte) 0xE0 << 12) ^
									((byte) 0x80 << 6) ^
									((byte) 0x80 << 0))));
						boolean isSurrogate = c >= '\uD800' && c < ('\uDFFF' + 1);
						if (isSurrogate) {
							return -1;
						} else {
							dst[dp++] = c;
						}
					}
					continue;
				}
				return -1;
			} else if ((b1 >> 3) == -2) {
				// 4 bytes, 21 bits: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
				if (off + 2 < sl) {
					int b2 = src[off++];
					int b3 = src[off++];
					int b4 = src[off++];
					int uc = ((b1 << 18) ^
						(b2 << 12) ^
						(b3 << 6) ^
						(b4 ^
							(((byte) 0xF0 << 18) ^
								((byte) 0x80 << 12) ^
								((byte) 0x80 << 6) ^
								((byte) 0x80 << 0))));
					if (((b2 & 0xc0) != 0x80 || (b3 & 0xc0) != 0x80 || (b4 & 0xc0) != 0x80) // isMalformed4
						||
						// shortest form check
						!(uc >= 0x010000 && uc < 0X10FFFF + 1) // !Character.isSupplementaryCodePoint(uc)
					) {
						return -1;
					} else {
						dst[dp++] = (char) ((uc >>> 10) + ('\uD800' - (0x010000 >>> 10))); // Character.highSurrogate(uc);
						dst[dp++] = (char) ((uc & 0x3ff) + '\uDC00'); // Character.lowSurrogate(uc);
					}
					continue;
				}
				return -1;
			} else {
				return -1;
			}
		}
		return dp;
	}

}
