package com.arthur.boot.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.arthur.common.utils.ClassUtils.convertClassNameToResourcePath;
import static org.springframework.core.io.support.ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;

/**
 * 类路径下扫描执行器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-23
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ClassPathScanningProcessor implements EnvironmentAware, ResourceLoaderAware, BeanPostProcessor, InstantiationAwareBeanPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ClassPathScanningProcessor.class);

    private String resourcePattern = "**/*.class";
    private Environment environment = new StandardEnvironment();
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	@Override
	public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
		if (bean instanceof ClassPathScanner classPathScanner) {
			this.scanCandidateClass(classPathScanner);
		}
		return bean;
	}


    public void scanCandidateClass(ClassPathScanner classPathScanner) {
        ClassPathScanner.Descriptor classPathDescriptor = classPathScanner.getClassPathDescriptor();
        String[] scanPackages = classPathDescriptor.getScanPackages();
        if (ObjectUtils.isEmpty(scanPackages)) {
            return;
        }

        Set<Class<?>> candidateClasses = new LinkedHashSet<>();
        for (String scanPackage : scanPackages) {
            try {
                scanCandidateClass(scanPackage, classPathScanner, candidateClasses);
            } catch (IOException e) {
                throw new BeanDefinitionStoreException("I/O failure during classpath scanning.", e);
            }
        }

        if (!CollectionUtils.isEmpty(candidateClasses)) {
            classPathScanner.handleCandidateClass(candidateClasses);
        }
    }

    protected void scanCandidateClass(String basePackage, ClassPathScanner classPathScanner,
            Set<Class<?>> candidateClasses) throws IOException {
        basePackage = environment.resolveRequiredPlaceholders(basePackage);
        String packageSearchPath = CLASSPATH_ALL_URL_PREFIX + convertClassNameToResourcePath(basePackage)
                + '/' + resourcePattern;
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        for (Resource resource : resources) {
            String filename = resource.getFilename();
            if (filename == null || !filename.contains(ClassUtils.CGLIB_CLASS_SEPARATOR)) {
                try {
                    this.loadCandidateClass(resource, classPathScanner, candidateClasses);
                } catch (FileNotFoundException e) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Ignore non-readable " + resource + ":" + e.getMessage());
                    }
                }
            }
        }
    }

    protected void loadCandidateClass(Resource resource, ClassPathScanner classPathScanner,
            Set<Class<?>> candidateClasses) throws IOException {
        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
        if (this.isCandidateComponent(metadataReader, classPathScanner)) {
            String className = metadataReader.getClassMetadata().getClassName();
            Class<?> clazz = ClassUtils.resolveClassName(className,
                    resourcePatternResolver.getClassLoader());
            candidateClasses.add(clazz);
        }
    }

    protected boolean isCandidateComponent(MetadataReader metadataReader, ClassPathScanner classPathScanner) throws IOException {
        ClassPathScanner.Descriptor descriptor = classPathScanner.getClassPathDescriptor();
        TypeFilter[] excludeFilters = descriptor.getExcludeFilters();
        if (!ObjectUtils.isEmpty(excludeFilters)) {
            for (TypeFilter excludeFilter : excludeFilters) {
                if (excludeFilter.match(metadataReader, metadataReaderFactory)) {
                    return false;
                }
            }
        }

        TypeFilter[] includeFilters = descriptor.getIncludeFilters();
        if (!ObjectUtils.isEmpty(includeFilters)) {
            for (TypeFilter includeFilter : includeFilters) {
                if (includeFilter.match(metadataReader, metadataReaderFactory)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setResourcePattern(String resourcePattern) {
        Assert.notNull(resourcePattern, "'resourcePattern' must not be null");
        this.resourcePattern = resourcePattern;
    }

    public MetadataReaderFactory getMetadataReaderFactory() {
        return metadataReaderFactory;
    }

    public ResourcePatternResolver getResourcePatternResolver() {
        return resourcePatternResolver;
    }

    public void setMetadataReaderFactory(@Nullable MetadataReaderFactory metadataReaderFactory) {
        this.metadataReaderFactory = metadataReaderFactory;
    }

    public void setResourcePatternResolver(@Nullable ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
    }

}
