package com.arthur.boot.mybatis.datasource;

import com.arthur.boot.mybatis.constant.MybatisConstants;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cache.CacheManager;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.Serial;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Spring AOP代理后置处理器
 *
 * @author DearYang
 * @date 2022-10-06
 * @since 1.0
 */
public class DatabaseAnnotationBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

	@Serial
	private static final long serialVersionUID = -3935533896974416128L;

	private final RouteLookup lookupKey;
	private final CacheManager cacheManager;
	private Class<? extends Annotation> annotationType;

	public DatabaseAnnotationBeanPostProcessor(RouteLookup lookupKey, CacheManager cacheManager) {
		this.lookupKey = lookupKey;
		this.cacheManager = cacheManager;
		super.setBeforeExistingAdvisors(true);
	}

	@Override
	public void setBeanFactory(@NonNull BeanFactory beanFactory) {
		super.setBeanFactory(beanFactory);
		DatabaseAnnotationAdvisor advisor = new DatabaseAnnotationAdvisor(lookupKey, cacheManager);
		if (annotationType != null) {
			advisor.setAnnotationType(this.annotationType);
		}
		advisor.setBeanFactory(beanFactory);
		this.advisor = advisor;
	}

	public void setAnnotationType(@Nullable Class<? extends Annotation> annotationType) {
		Assert.notNull(annotationType, "'annotationType' must not be null");
		if (this.checkAnnotationAvailable(annotationType)) {
			this.annotationType = annotationType;
		}
	}

	private boolean checkAnnotationAvailable(Class<? extends Annotation> annotationType) {
		Map<String, Object> attributes = MergedAnnotation.of(annotationType).asMap();
		return attributes.containsKey(MybatisConstants.ANNOTATION_NAME)
			&& attributes.containsKey(MybatisConstants.ANNOTATION_LOOK_UP_NAME);
	}

}
