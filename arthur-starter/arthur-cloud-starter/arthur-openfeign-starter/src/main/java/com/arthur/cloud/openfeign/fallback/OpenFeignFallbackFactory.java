package com.arthur.cloud.openfeign.fallback;

import com.arthur.cloud.openfeign.autoconfigure.FallbackProperties;
import feign.Target;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 实现OpenFeign的Fallback工厂类
 * <p>
 * 底层基于{@link Enhancer}代理，通过回调{@link OpenFeignFallback}完成
 *
 * @author DearYang
 * @date 2022-09-07
 * @see FallbackFactory
 * @since 1.0
 */
public class OpenFeignFallbackFactory<T> implements FallbackFactory<T> {

	private final Target<T> target;
	private final FallbackProperties fallbackProperties;

	public OpenFeignFallbackFactory(Target<T> target, FallbackProperties fallbackProperties) {
		this.target = target;
		this.fallbackProperties = fallbackProperties;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T create(Throwable cause) {
		Class<T> type = target.type();
		String name = target.name();
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(type);
		enhancer.setUseCache(true);
		enhancer.setCallback(new OpenFeignFallback<>(type, name, cause, fallbackProperties));
		return (T) enhancer.create();
	}

}
