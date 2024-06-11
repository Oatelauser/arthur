package com.arthur.boot.utils;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 基于CGLIB实现的两个类型相同的对象属性拷贝
 * <p>
 * Benchmark            Mode  Cnt     Score     Error   Units
 * beanUtils  thrpt    5     0.003 ±   0.001  ops/ns
 * cglib      thrpt    5     0.050 ±   0.001  ops/ns
 * beanUtils   avgt    5  2432.422 ± 208.606   ns/op
 * cglib       avgt    5   105.838 ±   9.229   ns/op
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-02
 * @since 1.0
 */
public class BeanCopierUtils {

	/**
	 * 缓存{@link BeanCopier}提高性能
	 */
	private static final Map<BeanCopierKey, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<>();

	/**
	 * 单个对象的复制
	 *
	 * @param source    源对象
	 * @param target    目标对象
	 * @param converter {@link Converter}
	 * @param <S>       源类型
	 * @param <T>       目标类型
	 * @return T
	 */
	public static <S, T> T copy(S source, T target, Converter converter) {
		BeanCopier beanCopier = getBeanCopier(source.getClass(), target.getClass(), converter);
		beanCopier.copy(source, target, converter);
		return target;
	}

	/**
	 * @see #copy(Object, Object, Converter)
	 */
	public static <S, T> T copy(S source, T target) {
		return copy(source, target, null);
	}

	/**
	 * Bean属性复制工具方法。
	 *
	 * @param sources 原始集合
	 * @param target  目标类
	 */
	public static <S, T> List<T> copyList(List<S> sources, T target) {
		if (CollectionUtils.isEmpty(sources)) {
			return Collections.emptyList();
		}

		List<T> list = new ArrayList<>(sources.size());
		BeanCopier beanCopier = getBeanCopier(sources.get(0).getClass(), target.getClass(), null);

		for (S source : sources) {
			beanCopier.copy(source, target, null);
			list.add(target);
		}
		return list;
	}

	public static BeanCopier getBeanCopier(Class<?> source, Class<?> target, Converter converter) {
		return BEAN_COPIERS.computeIfAbsent(new BeanCopierKey(source, target),
			key -> BeanCopier.create(source, target, converter != null));
	}

	private record BeanCopierKey(Class<?> source, Class<?> target) {
	}

}
