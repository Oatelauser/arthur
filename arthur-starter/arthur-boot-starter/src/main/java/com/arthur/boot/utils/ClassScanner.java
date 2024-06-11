package com.arthur.boot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.context.ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS;
import static org.springframework.core.io.support.ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;

/**
 * 基于Mybatis源码提供的加载class文件方式
 *
 * @author DearYang
 * @date 2022-09-21
 * @since 1.0
 */
public class ClassScanner {

	private static final Logger LOG = LoggerFactory.getLogger(ClassScanner.class);
	private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
	private static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();

	/**
	 * 参考实现{@code com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean#scanClasses}
	 *
	 * <pre>
	 *     {@code Set<Class<?>> classes = scanClasses("com.arthur", ValueExtractor.class);}
	 * </pre>
	 *
	 * @param packagePattern 包路径
	 * @param assignableType 需要加载的类
	 */
	public static Set<Class<?>> scanClasses(String packagePattern, Class<?> assignableType) throws IOException {
		Set<Class<?>> classes = new HashSet<>();
		String[] packagePatterns = StringUtils.tokenizeToStringArray(packagePattern, CONFIG_LOCATION_DELIMITERS);
		for (String locationPattern : packagePatterns) {
			Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(CLASSPATH_ALL_URL_PREFIX +
				ClassUtils.convertClassNameToResourcePath(locationPattern) + "/**/*.class");
			for (Resource resource : resources) {
				try {
					ClassMetadata classMetadata = METADATA_READER_FACTORY.getMetadataReader(resource).getClassMetadata();
					Class<?> clazz = ClassUtils.resolveClassName(classMetadata.getClassName(), null);
					if (assignableType == null || assignableType.isAssignableFrom(clazz)) {
						classes.add(clazz);
					}
				} catch (Throwable e) {
					if (LOG.isWarnEnabled()) {
						LOG.warn("Cannot load the [" + resource + "]. Cause by " + e);
					}
				}
			}
		}

		return classes;
	}

}
