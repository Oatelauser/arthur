package com.arthur.security.resource;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组合多个资源解析器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-05-08
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public class CompositeResourceParser implements ResourceParser<Object> {

    private final Map<Class, ResourceParser> resourceParsers = new HashMap<>(8);

    public CompositeResourceParser(List<ResourceParser<?>> resourceParsers) {
        this.registerResourceParser(resourceParsers);
    }

    @Override
    @SuppressWarnings("unchecked")
    public String parse(Object source) {
        if (source == null) {
            return "";
        }

        ResourceParser parser = resourceParsers.get(source.getClass());
        if (parser == null) {
            return "";
        }
        return parser.parse(source);
    }

    protected void registerResourceParser(List<ResourceParser<?>> parsers) {
        if (CollectionUtils.isEmpty(parsers)) {
            return;
        }

        Method method = ReflectionUtils.findMethod(ResourceParser.class, "parse", Object.class);
        Assert.notNull(method, "Cannot find method [parse] in ResourceParser interface");
        MethodParameter parameter = MethodParameter.forExecutable(method, 0);
        for (ResourceParser<?> parser : parsers) {
            Class<?> parameterType = parameter.withContainingClass(parser.getClass())
                    .getParameterType();
            resourceParsers.put(parameterType, parser);
        }
    }

}
