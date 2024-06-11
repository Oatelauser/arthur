package com.arthur.common.config;

import com.arthur.common.utils.StringUtils;

import java.util.Map;

/**
 * 注册器
 *
 * @author DearYang
 * @date 2022-09-16
 * @since 1.0
 */
public class Registry<T> {

	private final Map<String, T> storage;

	Registry(Map<String, T> storage) {
		this.storage = storage;
	}

	public T get(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}

		return storage.get(key);
	}

}
