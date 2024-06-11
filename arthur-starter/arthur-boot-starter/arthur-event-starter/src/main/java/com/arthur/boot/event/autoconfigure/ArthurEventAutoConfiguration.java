package com.arthur.boot.event.autoconfigure;

import com.arthur.boot.event.EventPublisherAware;
import com.arthur.boot.event.annotation.ArthurEventListener;
import com.arthur.boot.event.process.ArthurEventBusPostProcessor;
import com.arthur.boot.event.process.ArthurEventListenerAnnotationMethodProcessor;
import com.arthur.boot.process.AnnotationMethodProcessor;
import com.arthur.boot.process.AwareFactoryBean;
import com.arthur.common.notify.publisher.EventBus;
import com.arthur.common.notify.publisher.EventPublisher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 事件模型自动配置类
 *
 * @author DearYang
 * @date 2022-07-28
 * @since 1.0
 */
@AutoConfiguration
@EnableConfigurationProperties(value = ArthurEventProperties.class)
public class ArthurEventAutoConfiguration {

    @Bean(initMethod = "start")
    @ConditionalOnMissingBean
    public EventPublisher eventPublisher(ArthurEventProperties properties) {
        return new EventBus(properties.getThreadSize(), properties.getRingBufferSize());
    }

    @Bean
    public AwareFactoryBean eventPublisherAware() {
        return new AwareFactoryBean(EventPublisherAware.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public AnnotationMethodProcessor<ArthurEventListener> arthurEventListenerAnnotationMethodProcessor() {
        return new ArthurEventListenerAnnotationMethodProcessor();
    }

    @Bean
    public ArthurEventBusPostProcessor arthurEventPostProcessor() {
        return new ArthurEventBusPostProcessor();
    }

}
