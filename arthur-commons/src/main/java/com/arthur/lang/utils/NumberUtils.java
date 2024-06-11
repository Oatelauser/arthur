package com.arthur.lang.utils;

/**
 * 数字相关的工具类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-15
 * @since 1.0
 */
public abstract class NumberUtils {

	/**
	 * 位运算取绝对值
	 *
	 * @param number int数字
	 * @return 数字的绝对值
	 */
	public static int abs(int number) {
		return (number ^ (number >> 31)) - (number >> 31);
	}

	/**
	 * 位运算取绝对值
	 *
	 * @param number long数字
	 * @return 数字的绝对值
	 */
	public static long abs(long number) {
		return (number ^ (number >> 31)) - (number >> 31);
	}

	/**
	 * 位运算判断偶数
	 *
	 * @param number int数字
	 * @return true-偶数；false-奇数
	 */
	public boolean judgeOdd(int number) {
		return (number & 1) == 1;
	}

	/**
	 * 位运算判断偶数
	 *
	 * @param number long数字
	 * @return true-偶数；false-奇数
	 */
	public boolean judgeOdd(long number) {
		return (number & 1) == 1;
	}

}
