package com.arthur.oauth2.authentication;

import com.arthur.common.lifecycle.InitializeListener;
import com.arthur.oauth2.annotation.AuthPermission;
import com.arthur.oauth2.autoconfigure.OAuth2ResourceServerProperties;
import com.arthur.oauth2.constant.OAuth2Constants;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * 自定义认证注解处理器
 *
 * @author DearYang
 * @date 2022-08-14
 * @see AuthPermission
 * @since 1.0
 */
public class OAuth2AnnotationAnalysisProcessor implements InitializeListener {

    private final OAuth2ResourceServerProperties properties;
    private final RequestMappingHandlerMapping handlerMapping;

    public OAuth2AnnotationAnalysisProcessor(OAuth2ResourceServerProperties properties, RequestMappingHandlerMapping handlerMapping) {
        this.properties = properties;
        this.handlerMapping = handlerMapping;
    }

    @Override
    public void start() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        handlerMethods.forEach(this::processHandlerMethod);
    }

    protected void processHandlerMethod(RequestMappingInfo info, HandlerMethod handlerMethod) {
        AuthPermission methodAnnotation = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), AuthPermission.class);
        processHandlerMethod(info, handlerMethod, methodAnnotation);

        AuthPermission classAnnotation = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), AuthPermission.class);
        processHandlerMethod(info, handlerMethod, classAnnotation);
    }

    @SuppressWarnings("unused")
    protected void processHandlerMethod(RequestMappingInfo info, HandlerMethod handlerMethod, AuthPermission annotation) {
        if (annotation == null) {
            return;
        }

        Objects.requireNonNull(info.getPathPatternsCondition()).getPatternValues().forEach(this::addExcludeUrl);
    }

    private void addExcludeUrl(String url) {
        if (!StringUtils.hasText(url)) {
            properties.addExcludeUrl(url);
            return;
        }

        Matcher matcher = OAuth2Constants.PATH_VARIABLE_PATTERN.matcher(url);
        if (!matcher.find()) {
            properties.addExcludeUrl(url);
            return;
        }

        StringBuilder sb = new StringBuilder();
        do {
            matcher.appendReplacement(sb, Matcher.quoteReplacement(OAuth2Constants.PATH_VARIABLE_REPLACEMENT));
        } while (matcher.find());
        matcher.appendTail(sb);

        properties.addExcludeUrl(sb.toString());
    }

}
