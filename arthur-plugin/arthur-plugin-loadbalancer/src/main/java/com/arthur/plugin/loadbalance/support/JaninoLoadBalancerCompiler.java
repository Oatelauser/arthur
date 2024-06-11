package com.arthur.plugin.loadbalance.support;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.IClassBodyEvaluator;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.util.MultiValueMap;

import java.io.StringReader;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

/**
 * 一致性哈希动态值编译器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-16
 * @since 1.0
 */
public class JaninoLoadBalancerCompiler implements BeanClassLoaderAware {

	private ClassLoader classLoader;
	private final Cache<String, TemplateAdapter> templates = Caffeine.newBuilder()
		.softValues()
		.expireAfterAccess(Duration.ofMinutes(1))
		.build();

	public ConsistentHashCalculator getConsistentHashCalculator(String instanceKey, String body) {
		TemplateAdapter adapter = templates.getIfPresent(instanceKey);
		int identityHashCode = body.hashCode();
		if (adapter == null || adapter.identityHashCode != identityHashCode) {
			synchronized (templates) {
				adapter = templates.getIfPresent(instanceKey);
				if (adapter == null || adapter.identityHashCode != identityHashCode) {
					adapter = new TemplateAdapter(identityHashCode, this.getInstance(body));
					templates.put(instanceKey, adapter);
				}
			}
		}
		return adapter.template;
	}

	private ConsistentHashCalculator getInstance(String body) {
		String method = ConsistentHashCalculator.buildHashMethod(body);
		try {
			IClassBodyEvaluator classBodyEvaluator = CompilerFactoryFactory.getDefaultCompilerFactory(classLoader).newClassBodyEvaluator();
			classBodyEvaluator.setImplementedInterfaces(new Class<?>[]{ ConsistentHashCalculator.class });
			classBodyEvaluator.setDefaultImports(RequestData.class.getName(),
				HttpMethod.class.getName(), URI.class.getName(), Map.class.getName(),
				HttpHeaders.class.getName(), MultiValueMap.class.getName());
			return (ConsistentHashCalculator) classBodyEvaluator.createInstance(new StringReader(method));
		} catch (Exception e) {
			throw new IllegalStateException(method, e);
		}
	}

	@Override
	public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
		this.classLoader = Objects.requireNonNull(classLoader, "classLoader");
	}

	private record TemplateAdapter(int identityHashCode, ConsistentHashCalculator template) {
	}

}
