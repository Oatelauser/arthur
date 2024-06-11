package com.arthur.web.validation;

import com.arthur.boot.process.ClassPathScanner;
import jakarta.validation.Configuration;
import jakarta.validation.valueextraction.ValueExtractor;
import org.hibernate.validator.internal.engine.ConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Spring Validation配置类
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-04-23
 * @since 1.0
 */
public class SpringValidation implements ClassPathScanner, ValidationConfigurationCustomizer {

    private static final Logger LOG = LoggerFactory.getLogger(SpringValidation.class);

    private List<ValueExtractor<?>> valueExtractors;

    public SpringValidation(ObjectProvider<List<ValueExtractor<?>>> valueExtractors) {
        this.valueExtractors = valueExtractors.getIfAvailable();
    }

    @Override
    public Descriptor getClassPathDescriptor() {
        return new Descriptor(new String[]{ "com.arthur" },
                new TypeFilter[]{ new AssignableTypeFilter(ValueExtractor.class) });
    }

    @Override
    public void handleCandidateClass(Set<Class<?>> candidateClasses) {
        if (CollectionUtils.isEmpty(valueExtractors)) {
            valueExtractors = new ArrayList<>(candidateClasses.size());
        }

        for (Class<?> clazz : candidateClasses) {
            Constructor<?> constructor = ClassUtils.getConstructorIfAvailable(clazz);
            if (constructor != null) {
                try {
                    this.valueExtractors.add((ValueExtractor<?>) constructor.newInstance());
                } catch (Exception e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Unable to load the Value Extractor class [{}] because there " +
                                "is no no-argument constructor", clazz);
                    }
                }
            }
        }
    }

    @Override
    public void customize(Configuration<?> configuration) {
        if (!CollectionUtils.isEmpty(valueExtractors)) {
            valueExtractors.forEach(configuration::addValueExtractor);
        }

        if (configuration instanceof ConfigurationImpl configure) {
            configure.failFast(true);
        }
    }

}
