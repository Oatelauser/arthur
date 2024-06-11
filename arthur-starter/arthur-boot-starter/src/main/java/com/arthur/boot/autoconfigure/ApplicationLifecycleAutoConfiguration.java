package com.arthur.boot.autoconfigure;

import com.arthur.boot.lifecycle.*;
import com.arthur.boot.process.AwareBeanPostProcessor;
import com.arthur.boot.process.AwareFactoryBean;
import com.arthur.common.lifecycle.InitializeListener;
import com.arthur.common.lifecycle.ShutdownHook;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 服务上下线配置
 *
 * @author DearYang
 * @date 2022-08-01
 * @since 1.0
 */
@AutoConfiguration(after = ArthurBootAutoConfiguration.class)
public class ApplicationLifecycleAutoConfiguration {

    @Bean
    @ConditionalOnBean(value = AwareBeanPostProcessor.class)
    public AwareFactoryBean shutdownHandlerAware() {
        return new AwareFactoryBean(ShutdownHandlerAware.class);
    }

    @Bean
    @ConditionalOnBean(value = AwareBeanPostProcessor.class)
    public AwareFactoryBean serviceInitializerAware() {
        return new AwareFactoryBean(ServiceInitializerAware.class);
    }

	@Bean
	@ConditionalOnMissingBean
	public ShutdownHandler defaultGracefulShutdownHandler(ObjectProvider<List<ShutdownHook>> provider) {
		return new SmartGracefulShutdownHandler(provider);
	}

    @Bean
    @ConditionalOnMissingBean
    public ServiceInitializer defaultServiceInitializer(ObjectProvider<List<InitializeListener>> provider) {
        return new AbstractServiceInitializer(provider) {
            @Override
            protected void initialize() {
            }
        };
    }

}
