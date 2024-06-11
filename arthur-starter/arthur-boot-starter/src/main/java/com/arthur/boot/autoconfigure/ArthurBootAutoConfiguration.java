package com.arthur.boot.autoconfigure;

import com.arthur.boot.core.convert.ConversionServiceDelegator;
import com.arthur.boot.process.AnnotationListenerMethodPostProcessor;
import com.arthur.boot.process.AwareBeanPostProcessor;
import com.arthur.boot.process.AwareFactoryBean;
import com.arthur.boot.process.ClassPathScanningProcessor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Arthur Starter 自动配置
 *
 * @author DearYang
 * @date 2022-08-02
 * @since 1.0
 */
@AutoConfiguration
@Import({ SpringAwareConfiguration.class, UrlPathMatherConfiguration.class })
public class ArthurBootAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(GenericConversionService.class)
	public ConversionServiceFactoryBean conversionService(ObjectProvider<Set<Converter<?, ?>>> provider) {
		return new ConversionServiceDelegator(provider.getIfAvailable(Collections::emptySet));
	}

	@Bean
	@ConditionalOnMissingBean
	public AwareBeanPostProcessor awareBeanPostProcessor(List<AwareFactoryBean> factoryBeans) {
		return new AwareBeanPostProcessor(factoryBeans);
	}

	@Bean
	@ConditionalOnMissingBean
	public AnnotationListenerMethodPostProcessor annotationListenerMethodPostProcessor() {
		return new AnnotationListenerMethodPostProcessor();
	}

	@Bean
	@ConditionalOnMissingBean
	public ClassPathScanningProcessor classPathScanningProcessor() {
		return new ClassPathScanningProcessor();
	}

}
