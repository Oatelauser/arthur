package com.arthur.boot.core.convert;

import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.lang.NonNull;

import java.util.Set;

/**
 * 类型转换委托器
 *
 * @author DearYang
 * @date 2022-07-29
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ConversionServiceDelegator extends ConversionServiceFactoryBean {

    /**
     * 自定义转换器实现
     */
    private final Set<Converter<?, ?>> converters;

    public ConversionServiceDelegator(Set<Converter<?, ?>> converters) {
        this.converters = converters;
    }

    /**
     * 使用全局默认的转换器
     *
     * @return 通用转换器
     */
    @Override
    @NonNull
    protected GenericConversionService createConversionService() {
        return (DefaultConversionService) DefaultConversionService.getSharedInstance();
    }

    /**
     * 注册自定义的转换器
     */
    @Override
    public void afterPropertiesSet() {
        setConverters(converters);
        super.afterPropertiesSet();
    }

}
