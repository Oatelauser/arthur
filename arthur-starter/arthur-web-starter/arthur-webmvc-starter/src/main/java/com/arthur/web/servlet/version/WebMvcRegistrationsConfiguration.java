package com.arthur.web.servlet.version;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

/**
 * 配置API版本
 *
 * @author DearYang
 * @date 2022-10-12
 * @since 1.0
 */
public class WebMvcRegistrationsConfiguration implements WebMvcRegistrations, ImportAware {

    private AnnotationAttributes annotationAttributes;

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new RequestVersionMappingHandlerMapping(annotationAttributes);
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        Map<String, Object> attributes = importMetadata.getAnnotationAttributes(EnableRequestMappingVersion.class.getName());
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(attributes);
        if (annotationAttributes == null) {
            throw new IllegalArgumentException("@EnableRequestVersion is not present on importing class "
                    + importMetadata.getClassName());
        }
        this.annotationAttributes = annotationAttributes;
    }

}
