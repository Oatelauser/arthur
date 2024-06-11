package com.arthur.web.servlet.version;

import com.arthur.boot.utils.AnnotationUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.lang.NonNull;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

/**
 * 自定义{@link RequestMappingHandlerMapping}，加入请求版本判断逻辑
 *
 * @author DearYang
 * @date 2022-10-12
 * @since 1.0
 */
@SuppressWarnings("all")
public class RequestVersionMappingHandlerMapping extends RequestMappingHandlerMapping {

    private static final String VERSION = "version";

    private final String version;
    private final String originalVersion;

    public RequestVersionMappingHandlerMapping(AnnotationAttributes annotationAttributes) {
        String version = annotationAttributes.getString("value");
        this.originalVersion = this.selectVersion(version);
        this.version = "{" + this.originalVersion + "}";
    }

    @Override
    protected RequestCondition<?> getCustomTypeCondition(@NonNull Class<?> handlerType) {
        String[] urls = this.selectMatchedMappingUrl(handlerType);
        if (urls == null) {
            return null;
        }
        RequestMappingVersion requestMappingVersion = findAnnotation(handlerType, RequestMappingVersion.class);
        if (requestMappingVersion == null) {
            return null;
        }

        for (int i = 0; i < urls.length; i++) {
            urls[i] = this.createAntPathWithSuffixWildcard(urls[i]);
        }
        return new RequestVersionRequestCondition(originalVersion, urls, requestMappingVersion);
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(@NonNull Method method) {
        String[] urls = this.selectMatchedMappingUrl(method);
        if (urls == null) {
            return null;
        }
        RequestMappingVersion requestMappingVersion = AnnotationUtils.findMethodAnnotation(method, RequestMappingVersion.class, method.getDeclaringClass());
        if (requestMappingVersion == null) {
            return null;
        }

        for (int i = 0; i < urls.length; i++) {
            urls[i] = this.createAntPathWithPrefixWildcard(urls[i]);
        }
        return new RequestVersionRequestCondition(originalVersion, urls, requestMappingVersion);
    }


    private String[] selectMatchedMappingUrl(AnnotatedElement annotatedElement) {
        RequestMapping annotation = AnnotatedElementUtils.findMergedAnnotation(annotatedElement, RequestMapping.class);
        if (annotation == null) {
            return null;
        }
        String[] urls = this.selectMatchedMappingUrl(annotation);
        if (ObjectUtils.isEmpty(urls)) {
            return null;
        }
        return urls;
    }

    private String[] selectMatchedMappingUrl(RequestMapping annotation) {
        String[] mappingUrls = annotation.value();
        if (ObjectUtils.isEmpty(mappingUrls)) {
            return null;
        }

        return Arrays.stream(annotation.value())
                .filter(url -> url.contains(this.version))
                .toArray(String[]::new);
    }

    private String createAntPathWithPrefixWildcard(String url) {
        StringBuilder antUrlBuilder = new StringBuilder();
        antUrlBuilder.append("/**");
        if (!url.startsWith("/")) {
            antUrlBuilder.append("/");
        }
        return antUrlBuilder.append(url).toString();
    }

    private String createAntPathWithSuffixWildcard(String url) {
        StringBuilder antUrlBuilder = new StringBuilder();
        if (!url.startsWith("/")) {
            antUrlBuilder.append("/");
        }
        antUrlBuilder.append(url);
        if (!url.endsWith("/")) {
            antUrlBuilder.append("/");
        }
        return antUrlBuilder.append("**").toString();
    }

    private String selectVersion(String version) {
        if (!StringUtils.hasText(version)) {
            version = VERSION;
        }
        return version;
    }

}
