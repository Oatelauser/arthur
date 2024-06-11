package com.arthur.plugin.circuitbreaker.key;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link KeyResolver}工厂类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-20
 * @since 1.0
 */
public class KeyResolverFactory {

	private final Map<String, KeyResolver> keyResolvers;

	public KeyResolverFactory(List<KeyResolver> resolvers) {
		this.keyResolvers = obtainKeyResolvers(resolvers);
	}

	public KeyResolver get(String name) {
		return keyResolvers.get(name);
	}

	private Map<String, KeyResolver> obtainKeyResolvers(List<KeyResolver> resolvers) {
		Map<String, KeyResolver> keyResolvers = new HashMap<>(resolvers.size());
		for (KeyResolver keyResolver : resolvers) {
			String name;
			if (keyResolver instanceof NamedKeyResolver resolver) {
				name = resolver.name();
			} else {
				name = NamedKeyResolver.resolveName(keyResolver.getClass());
			}
			keyResolvers.put(name, keyResolver);
		}
		return keyResolvers;
	}

}
