package com.arthur.common.algorithms;

import com.arthur.common.utils.NumberUtils;

/**
 * <a href="https://cloud.tencent.com/developer/article/1432686?from=15425">常见哈希算法</a>
 *
 * @author DearYang
 * @date 2022-07-27
 * @since 1.0
 */
@SuppressWarnings("SpellCheckingInspection")
public abstract class HashAlgorithm {

	private static final int FNV132_SEED = 16777619;
	private static final int BKDR_SEED = 131;

	/**
	 * 32位FNV1_32_HASH算法计算Hash值，一般用于一致性Hash计算
	 */
	public static int fnv132Hash(String str) {
		int hash = (int) 2166136261L;
		for (int i = 0; i < str.length(); i++) {
			hash = (hash ^ str.charAt(i)) * FNV132_SEED;
		}
		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;

		// 如果算出来的值为负数则取其绝对值
		return hash < 0 ? NumberUtils.abs(hash) : hash;
	}

	/**
	 * BKDR Hash算法
	 * <p>
	 * 100000个不同字符串产生的冲突数，大概在0~3波动，使用100百万不同的字符串，冲突数大概110+范围波动
	 */
	public static int bkdrHash(String str) {
		int hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = hash * BKDR_SEED + str.charAt(i);
		}
		return hash & 0x7FFFFFFF;
	}

	/**
	 * AP Hash算法
	 */
	public static int apHash(String str) {
		int hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash ^= ((i & 1) == 0) ? ((hash << 7) ^ str.charAt(i) ^ (hash >> 3)) :
				(~((hash << 11) ^ str.charAt(i) ^ (hash >> 5)));
		}
		return hash;
	}

}
