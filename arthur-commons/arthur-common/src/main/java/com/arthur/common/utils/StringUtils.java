package com.arthur.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * string util
 *
 * @author DearYang
 * @date 2022-07-13
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class StringUtils {

	/**
	 * 字符串是否为空
	 *
	 * @param str {@link String}
	 * @return true or false
	 */
	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		}
		return str.isEmpty();
	}

	/**
	 * 是否有长度
	 *
	 * @param str 字符串
	 * @return true or false
	 */
	public static boolean hasLength(String str) {
		return !isEmpty(str);
	}

	/**
	 * 是否空字符串
	 *
	 * @param str 字符串
	 * @return 是否空字符串
	 */
	public static boolean isBlank(String str) {
		if (str == null) {
			return true;
		}
		return str.isBlank();
	}

	/**
	 * 判断是否数字
	 *
	 * @param str 字符串
	 * @return 判断字符串是否全是数字
	 */
	public static boolean isNumber(String str) {
		for (int i = 0; i < str.length(); ++i) {
			char ch = str.charAt(i);
			if (ch == '+' || ch == '-') {
				if (i != 0) {
					return false;
				}
			} else if (ch < '0' || ch > '9') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否有非空白的字符串
	 *
	 * @param str 字符串
	 * @return true or false
	 */
	public static boolean hasText(String str) {
		return !isBlank(str);
	}

	public static String getOrDefault(String value, String defaultValue) {
		if (isEmpty(value)) {
			return defaultValue;
		}

		return value;
	}

	/**
	 * 将中文字符转换为 Unicode。
	 *
	 * @param str 字符串
	 * @return 转换后的字符串
	 */
	public static String chinesStrToUnicode(String str) {
		StringBuilder sb = new StringBuilder();
		char[] chars = str.toCharArray();
		for (char aChar : chars) {
			if (isBaseLetter(aChar)) {
				sb.append(aChar);
			} else {
				sb.append(String.format("\\u%04x", (int) aChar));
			}
		}
		return sb.toString();
	}

	/**
	 * 字符是latin编码的字符、空白字符
	 *
	 * @param ch 字符
	 * @return true or false
	 */
	public static boolean isBaseLetter(char ch) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
		return ub == Character.UnicodeBlock.BASIC_LATIN || Character.isWhitespace(ch);
	}

	/**
	 * 去除左边的指定字符串
	 *
	 * @param str    字符串
	 * @param prefix 前缀字符串
	 * @return 去除之后的字符串
	 */
	public static String lstrip(String str, String prefix) {
		if (str == null || str.isEmpty()) {
			return str;
		}

		if (prefix == null || prefix.isEmpty()) {
			return str;
		}

		int index = str.indexOf(prefix);
		if (index == 0) {
			str = str.substring(0, prefix.length());
		}

		return str;
	}

	/**
	 * 高性能字符串分割，比原生JDK的split性能更高，自定义分割函数，返回第一个
	 *
	 * @param str   待分割的字符串
	 * @param delimiter 分隔符
	 * @return 分割后的第一个字符串
	 */
	public static String splitFirst(final String str, final String delimiter) {
		if (null == str || StringUtils.isEmpty(delimiter)) {
			return str;
		}

		int index = str.indexOf(delimiter);
		if (index < 0) {
			return str;
		}
		if (index == 0) {
			// 一开始就是分隔符，返回空串
			return "";
		}

		return str.substring(0, index);
	}

	/**
	 * 高性能字符串分割，比原生JDK的split性能更高，自定义分割函数，返回全部
	 *
	 * @param str   待分割的字符串
	 * @param delimiter 分隔符
	 * @return 分割后的返回结果
	 */
	public static List<String> split(String str, final String delimiter) {
		if (null == str) {
			return new ArrayList<>(0);
		}

		if (StringUtils.isEmpty(delimiter)) {
			List<String> result = new ArrayList<>(1);
			result.add(str);

			return result;
		}

		final List<String> stringList = new ArrayList<>();
		while (true) {
			int index = str.indexOf(delimiter);
			if (index < 0) {
				stringList.add(str);
				break;
			}
			stringList.add(str.substring(0, index));
			str = str.substring(index + delimiter.length());
		}
		return stringList;
	}

	/**
	 * <p>Compares two CharSequences, returning {@code true} if they represent
	 * equal sequences of characters, ignoring case.</p>
	 *
	 * <p>{@code null}s are handled without exceptions. Two {@code null}
	 * references are considered equal. Comparison is case insensitive.</p>
	 *
	 * <pre>
	 * StringUtils.equalsIgnoreCase(null, null)   = true
	 * StringUtils.equalsIgnoreCase(null, "abc")  = false
	 * StringUtils.equalsIgnoreCase("abc", null)  = false
	 * StringUtils.equalsIgnoreCase("abc", "abc") = true
	 * StringUtils.equalsIgnoreCase("abc", "ABC") = true
	 * </pre>
	 *
	 * @param str1 the first CharSequence, may be null
	 * @param str2 the second CharSequence, may be null
	 * @return {@code true} if the CharSequence are equal, case insensitive, or both {@code null}
	 * @since 3.0 Changed signature from equalsIgnoreCase(String, String) to equalsIgnoreCase(CharSequence,
	 * CharSequence)
	 */
	public static boolean equalsIgnoreCase(final CharSequence str1, final CharSequence str2) {
		if (str1 == null || str2 == null) {
			return str1 == str2;
		} else if (str1 == str2) {
			return true;
		} else if (str1.length() != str2.length()) {
			return false;
		} else {
			return CharSequenceUtils.regionMatches(str1, true, 0, str2, 0, str1.length());
		}
	}

	@SuppressWarnings("all")
	static class CharSequenceUtils {

		/**
		 * Green implementation of regionMatches.
		 *
		 * @param cs         the {@code CharSequence} to be processed
		 * @param ignoreCase whether or not to be case insensitive
		 * @param thisStart  the index to start on the {@code cs} CharSequence
		 * @param substring  the {@code CharSequence} to be looked for
		 * @param start      the index to start on the {@code substring} CharSequence
		 * @param length     character length of the region
		 * @return whether the region matched
		 */
		static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
									 final CharSequence substring, final int start, final int length) {
			if (cs instanceof String && substring instanceof String) {
				return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
			}
			int index1 = thisStart;
			int index2 = start;
			int tmpLen = length;

			while (tmpLen-- > 0) {
				final char c1 = cs.charAt(index1++);
				final char c2 = substring.charAt(index2++);

				if (c1 == c2) {
					continue;
				}

				if (!ignoreCase) {
					return false;
				}

				// The same check as in String.regionMatches():
				if (Character.toUpperCase(c1) != Character.toUpperCase(c2) && Character.toLowerCase(c1) != Character
					.toLowerCase(c2)) {
					return false;
				}
			}

			return true;
		}

	}

}
