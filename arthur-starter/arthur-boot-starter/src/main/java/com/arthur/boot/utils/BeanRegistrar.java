package com.arthur.boot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.beans.Introspector;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Bean注册相关
 *
 * @author DearYang
 * @date 2022-07-29
 * @since 1.0
 */
@SuppressWarnings("unused")
public class BeanRegistrar {

    private static final Logger LOG = LoggerFactory.getLogger(BeanRegistrar.class);

    /**
     * 注册基础的Bean
     *
     * @param beanName bean的名称
     * @param beanType bean的类型
     * @param registry {@link BeanDefinitionRegistry}注册器
     * @return 是否注册成功
     */
    public static boolean registerInfrastructureBean(String beanName, Class<?> beanType, BeanDefinitionRegistry registry) {
        if (registry.containsBeanDefinition(beanName)) {
            return false;
        }

        RootBeanDefinition beanDefinition = new RootBeanDefinition(beanType);
        beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        registry.registerBeanDefinition(beanName, beanDefinition);
        if (LOG.isInfoEnabled()) {
            LOG.info("Thr infrastructure bean definition [{}] with bean name [{}}] has been registered", beanDefinition, beanName);
        }

        return true;
    }

    /**
     * 从{@link  SpringFactoriesLoader}注册 bean
     *
     * @param registry     {@link BeanDefinitionRegistry}
     * @param factoryClass {@link org.springframework.beans.factory.FactoryBean}
     * @return 注册成功个数
     */
    public static int registerFactoryBean(BeanDefinitionRegistry registry, Class<?> factoryClass) {
        int count = 0;
        ClassLoader classLoader = registry.getClass().getClassLoader();
        List<String> factoryImplClassNames = SpringFactoriesLoader.loadFactoryNames(factoryClass, classLoader);
        for (String factoryImplClassName : factoryImplClassNames) {
            Class<?> factoryImplClass = ClassUtils.resolveClassName(factoryImplClassName, classLoader);
            String beanName = Introspector.decapitalize(ClassUtils.getShortName(factoryImplClassName));
            if (registerInfrastructureBean(beanName, factoryImplClass, registry)) {
                count++;
            } else {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("The factory class bean [{}] has been registered with ban name [{}]", factoryImplClass, beanName);
                }
            }
        }

        return count;
    }

    /**
     * 注册bean
     *
     * @param registry         {@link BeanDefinitionRegistry}
     * @param annotatedClasses 注解标注的bean
     */
    public static void registerBeans(BeanDefinitionRegistry registry, Class<?>... annotatedClasses) {
        if (ObjectUtils.isEmpty(annotatedClasses)) {
            return;
        }

        for (Class<?> annotatedClass : annotatedClasses) {
            registerBean(registry, annotatedClass);
        }
    }

    /**
     * 注册bean
     *
     * @param registry       {@link BeanDefinitionRegistry}
     * @param annotatedClass 注解标注的bean
     */
    public static void registerBean(BeanDefinitionRegistry registry, Class<?> annotatedClass) {
        if (ObjectUtils.isEmpty(annotatedClass)) {
            return;
        }
        if (BeanUtils.isPresentAnnotatedBean(registry, annotatedClass)) {
            return;
        }

        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(registry);
        reader.registerBean(annotatedClass);

        if (LOG.isDebugEnabled()) {
            LOG.debug("{} register annotated classed {}", registry.getClass().getSimpleName(), annotatedClass);
        }
    }

    /**
     * 扫描指定的包路径
     *
     * @param registry {@link BeanDefinitionRegistry}
     * @param packages 包路径
     * @return 新增的bean个数
     */
    public static int scanBasePackages(BeanDefinitionRegistry registry, String... packages) {
        if (ObjectUtils.isEmpty(packages)) {
            return 0;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("{} will scan base package {}", registry.getClass().getSimpleName(), String.join(", ", packages));
        }


        Set<String> registerBeanNames = new HashSet<>(50);
        Collections.addAll(registerBeanNames, registry.getBeanDefinitionNames());
        ClassPathBeanDefinitionScanner beanScanner = new ClassPathBeanDefinitionScanner(registry);
        int count = beanScanner.scan(packages);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Scan component count [{}] under base packages [{}]", count, String.join(", ", packages));
        }

        Set<String> scannedBeanNames = new HashSet<>(count);
        Collections.addAll(scannedBeanNames, registry.getBeanDefinitionNames());

        scannedBeanNames.removeAll(registerBeanNames);

        for (String scannedBeanName : scannedBeanNames) {
            BeanDefinition scannedBeanDefinition = registry.getBeanDefinition(scannedBeanName);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Scan component: {}, class: {}", scannedBeanName, scannedBeanDefinition.getBeanClassName());
            }
        }

        return scannedBeanNames.size();
    }

}
