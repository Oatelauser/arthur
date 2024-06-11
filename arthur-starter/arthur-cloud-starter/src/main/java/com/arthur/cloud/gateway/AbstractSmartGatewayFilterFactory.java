package com.arthur.cloud.gateway;

import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 智能网关过滤器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
@SuppressWarnings("unused")
public abstract class AbstractSmartGatewayFilterFactory<C> extends AbstractGatewayFilterFactory<C> {

	private final Class<C> configClass;
	private List<String> shortcutField;

	public AbstractSmartGatewayFilterFactory() {
		super();
		this.configClass = this.resolveConfigClass();
	}

	public AbstractSmartGatewayFilterFactory(Class<C> configClass) {
		super(null);
		this.configClass = Objects.requireNonNull(configClass, "configClass");
	}

	@Override
	public List<String> shortcutFieldOrder() {
		if (shortcutField == null) {
			shortcutField = this.resolveShortcutField();
		}
		return shortcutField;
	}

	@Override
	public Class<C> getConfigClass() {
		return this.configClass;
	}

	@Override
	public C newConfig() {
		return BeanUtils.instantiateClass(this.configClass);
	}

	@SuppressWarnings("unchecked")
	private Class<C> resolveConfigClass() {
		Class<?>[] classes = GenericTypeResolver.resolveTypeArguments(getClass(),
			AbstractSmartGatewayFilterFactory.class);
		if (ObjectUtils.isEmpty(classes)) {
			return (Class<C>) Object.class;
		}

		return (Class<C>) classes[0];
	}

	@SuppressWarnings("unchecked")
	private List<String> resolveShortcutField() {
		BeanMap.Generator bean = new BeanMap.Generator();
		bean.setRequire(BeanMap.REQUIRE_GETTER + BeanMap.REQUIRE_SETTER);
		bean.setBeanClass(this.configClass);
		BeanMap beanMap = bean.create();
		return List.copyOf((Set<String>) beanMap.keySet());
	}

}
