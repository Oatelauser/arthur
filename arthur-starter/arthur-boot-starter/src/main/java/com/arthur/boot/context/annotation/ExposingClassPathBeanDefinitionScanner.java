package com.arthur.boot.context.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import static org.springframework.context.annotation.AnnotationConfigUtils.registerAnnotationConfigProcessors;

/**
 * A extension class of {@link ClassPathBeanDefinitionScanner} to expose some methods:
 * <ul>
 *     <li>{@link ClassPathBeanDefinitionScanner#doScan(String...)}</li>
 *     <li>{@link ClassPathBeanDefinitionScanner#checkCandidate(String, BeanDefinition)}</li>
 * </ul>
 * <p>
 * {@link ExposingClassPathBeanDefinitionScanner} also supports the features from {@link #getRegistry() BeanDefinitionRegistry}
 * and {@link #getSingletonBeanRegistry() SingletonBeanRegistry}
 *
 * @see ClassPathBeanDefinitionScanner
 * @see BeanDefinitionRegistry
 * @see SingletonBeanRegistry
 * @since 1.0.6
 */
@SuppressWarnings("unused")
public class ExposingClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    private static final Logger LOG = LoggerFactory.getLogger(ExposingClassPathBeanDefinitionScanner.class);

    public ExposingClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters,
                                                  Environment environment, ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment);
        setResourceLoader(resourceLoader);
        registerAnnotationConfigProcessors(registry);
    }

    public SingletonBeanRegistry getSingletonBeanRegistry() {
        return (SingletonBeanRegistry) getRegistry();
    }

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        BeanDefinitionRegistry registry = getRegistry();
        if (registry == null) {
            LOG.warn("BeanDefinitionRegistry is null cant registry");
            return;
        }
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    public void registerSingleton(String beanName, Object singletonObject) {
        getSingletonBeanRegistry().registerSingleton(beanName, singletonObject);
    }

}
