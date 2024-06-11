package com.arthur.boot.mybatis.datasource;

import com.arthur.boot.aop.AnnotationClassOrMethodFilter;
import com.arthur.boot.aop.PublicMethodFilter;
import com.arthur.boot.mybatis.annotation.DS;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cache.CacheManager;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.lang.annotation.Annotation;

import static com.arthur.boot.aop.AnnotationExpandMatchingPointcut.createClassOrMethodAnnotationPointcut;

/**
 * 动态数据源Advisor
 *
 * @author DearYang
 * @date 2022-09-24
 * @since 1.0
 */
public class DatabaseAnnotationAdvisor extends AbstractBeanFactoryPointcutAdvisor {

	@Serial
	private static final long serialVersionUID = 6537514933520048781L;

	private BeanFactory beanFactory;
	private Class<? extends Annotation> annotationType = DS.class;

	public DatabaseAnnotationAdvisor(RouteLookup lookupKey, CacheManager cacheManager) {
		AnnotationDatabaseInterceptor databaseInterceptor = new AnnotationDatabaseInterceptor(lookupKey, cacheManager);
		databaseInterceptor.setAnnotationType(annotationType);
		databaseInterceptor.setBeanFactory(beanFactory);
		this.setAdvice(databaseInterceptor);
		this.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
	}

	@NonNull
	@Override
	public Pointcut getPointcut() {
		AnnotationClassOrMethodFilter classFilter = new AnnotationClassOrMethodFilter(this.annotationType);
		classFilter.setMethodFilter(new PublicMethodFilter());
		return createClassOrMethodAnnotationPointcut(classFilter);
	}

	public void setAnnotationType(Class<? extends Annotation> annotationType) {
		this.annotationType = annotationType;
	}

	@Override
	public void setBeanFactory(@NonNull BeanFactory beanFactory) {
		super.setBeanFactory(beanFactory);
		this.beanFactory = beanFactory;
	}

}
