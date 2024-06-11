package com.arthur.common.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册器构造器
 *
 * @author DearYang
 * @date 2022-09-16
 * @since 1.0
 */
public class RegistryBuilder<T> {

	private final Map<String, T> items;

	RegistryBuilder(Map<String, T> items) {
		this.items = items;
	}

	public RegistryBuilder<T> register(String id, T item) {
		items.put(id, item);
		return this;
	}

	public Registry<T> build() {
		return new Registry<>(items);
	}

	public static <T> RegistryBuilder<T> create() {
		return new RegistryBuilder<>(new HashMap<>());
	}

	public static <T> RegistryBuilder<T> create(Map<String, T> items) {
		return new RegistryBuilder<>(items);
	}

}
