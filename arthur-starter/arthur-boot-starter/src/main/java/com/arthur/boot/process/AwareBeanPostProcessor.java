package com.arthur.boot.process;

import com.arthur.boot.constant.BootConstants;
import com.arthur.common.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * 自定义实现可拓展的Spring Aware接口注入
 * <p>
 * 1.注册接口必须是interface
 * 2.注册接口必须实现{@link Aware}
 * 3.注册接口必须只有一个方法
 * 4.注册接口方法名必须是setXXX，不能有返回值
 * 4.注册接口方法必须只有一个参数
 * 5.方法参数必须是注入Bean的子类或本身
 *
 * @author DearYang
 * @date 2022-08-02
 * @since 1.0
 */
public class AwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor, ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(AwareBeanPostProcessor.class);

	private final boolean strictly;
	private final List<AwareFactoryBean> factoryBeans;
	private ApplicationContext applicationContext;

	public AwareBeanPostProcessor(List<AwareFactoryBean> factoryBeans) {
		this(false, factoryBeans);
	}

	public AwareBeanPostProcessor(boolean strictly, List<AwareFactoryBean> factoryBeans) {
		this.strictly = strictly;
		this.factoryBeans = this.getAwareFactoryBeans(factoryBeans);
	}

	@Override
	public boolean postProcessAfterInstantiation(@NonNull Object bean, @NonNull String beanName) throws BeansException {
		if (!CollectionUtils.isEmpty(factoryBeans) && bean instanceof Aware) {
			processAwareBeans(beanName, bean);
		}
		return true;
	}

	public void processAwareBeans(String beanName, Object bean) {
		for (AwareFactoryBean factoryBean : factoryBeans) {
			ResolvableType declaredType = factoryBean.getDeclaredType();
			if (declaredType.isAssignableFrom(ResolvableType.forInstance(bean))) {
				processAwareBean(beanName, bean, factoryBean);
			}
		}
	}

	@SuppressWarnings("unused")
	private void processAwareBean(String beanName, Object bean, AwareFactoryBean factoryBean) {
		Class<?> declaredClass = factoryBean.getDeclaredType().toClass();
		Method method = declaredClass.getMethods()[0];
		ResolvableType parameterType = ResolvableType.forMethodParameter(method, 0, bean.getClass());
		Class<?> beanType = parameterType.toClass();
		if (strictly && !additionalStrictChecks(beanType, method)) {
			return;
		}

		Object awareBean = applicationContext.getBeanProvider(parameterType.toClass()).getIfAvailable();
		if (awareBean == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Can`t get bean by [{}]", declaredClass);
			}
			return;
		}

		try {
			method.invoke(bean, awareBean);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Aware Interface [{}] auto set instance to bean [{}]", declaredClass, bean);
			}
		} catch (IllegalAccessException | InvocationTargetException ignored) {
			// 不可能会发生异常，前面已经做了校验
		}
	}

	private List<AwareFactoryBean> getAwareFactoryBeans(List<AwareFactoryBean> factoryBeans) {
		for (Iterator<AwareFactoryBean> iterator = factoryBeans.iterator(); iterator.hasNext(); ) {
			AwareFactoryBean factoryBean = iterator.next();
			if (!additionalAwareChecks(factoryBean)) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("AwareDescriptor is non compliance with specifications [{}]", factoryBean);
				}
				iterator.remove();
			}
		}

		return factoryBeans;
	}

	private boolean additionalAwareChecks(AwareFactoryBean factoryBean) {
		Class<?> awareInterface = factoryBean.getDeclaredType().toClass();
		if (!awareInterface.isInterface()) {
			return false;
		}
		if (!Aware.class.isAssignableFrom(awareInterface)) {
			return false;
		}
		// 检查方法
		Method[] methods = awareInterface.getMethods();
		if (methods.length != 1) {
			return false;
		}

		// 检查方法参数
		Method method = methods[0];
		Class<?>[] parameterTypes = method.getParameterTypes();
		return parameterTypes.length == 1;
	}

	private boolean additionalStrictChecks(Class<?> beanType, Method method) {
		String methodName = BootConstants.AWARE_METHOD_PREFIX + beanType.getSimpleName();
		if (!methodName.equals(method.getName())) {
			return false;
		}

		return void.class.equals(method.getReturnType());
	}

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
