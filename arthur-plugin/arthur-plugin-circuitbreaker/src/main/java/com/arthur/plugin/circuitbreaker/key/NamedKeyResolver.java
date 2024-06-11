package com.arthur.plugin.circuitbreaker.key;

import com.arthur.boot.utils.NameUtils;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;

/**
 * 具名key解析器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-20
 * @since 1.0
 */
public interface NamedKeyResolver extends KeyResolver {

	/**
	 * 解析注册的名称
	 */
	default String name() {
		return resolveName(this.getClass());
	}

	/**
	 * 解析名称工具方法
	 */
	static String resolveName(Class<? extends KeyResolver> klass) {
		return NameUtils.normalizeClassName(KeyResolver.class, klass);
	}

}
