package com.arthur.boot.context;

import com.arthur.boot.utils.BeanUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * spring context holder
 *
 * @author DearYang
 * @date 2022-07-29
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ApplicationContextHolder implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static ApplicationContextHolder INSTANCE;
	/**
	 * spring context
	 */
	private ConfigurableApplicationContext applicationContext;

	public ApplicationContextHolder() {
		INSTANCE = this;
	}

	/**
	 * 获取Spring上下文对象
	 *
	 * @return {@link ConfigurableApplicationContext}
	 */
	@Nullable
	public static ConfigurableApplicationContext get() {
		return INSTANCE == null ? null : INSTANCE.applicationContext;
	}

	/**
	 * 获取Spring Bean对象
	 *
	 * @param beanType bean的类型
	 * @param <T>      实例泛型
	 * @return T
	 */
	public static <T> T getBean(Class<T> beanType) {
		return Optional.ofNullable(get())
			.map(context -> context.getBean(beanType))
			.orElse(null);
	}

	/**
	 * 获取Spring Bean对象
	 *
	 * @param beanName bean的名称
	 * @param <T>      实例泛型
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		return Optional.ofNullable(get())
			.map(context -> (T) context.getBean(beanName))
			.orElse(null);
	}

	/**
	 * 获取Spring Bean对象
	 *
	 * @param beanType bean的类型
	 * @param <T>      实例泛型
	 * @return T
	 */
	public static <T> T getBean(String beanName, Class<T> beanType) {
		return Optional.ofNullable(get())
			.map(context -> BeanFactoryAnnotationUtils.qualifiedBeanOfType(context, beanType, beanName))
			.orElse(null);
	}

	/**
	 * 获取带有泛型的Bean对象
	 *
	 * @param clazz    Bean的类型
	 * @param generics Bean的类型泛型
	 * @param <T>      Bean的泛型
	 * @return 具有泛型的Bean对象
	 */
	public static <T> T getBean(Class<?> clazz, Class<?>... generics) {
		ResolvableType type = ResolvableType.forClassWithGenerics(clazz, generics);
		return getBean(type);
	}

	/**
	 * @see BeanUtils#getBeanOfGenerics(ListableBeanFactory, ResolvableType)
	 */
	public static <T> T getBean(ResolvableType type) {
		ConfigurableApplicationContext context = get();
		if (context == null) {
			return null;
		}

		return BeanUtils.getBeanOfGenerics(context, type);
	}

	@SuppressWarnings("unused")
	public ConfigurableApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
