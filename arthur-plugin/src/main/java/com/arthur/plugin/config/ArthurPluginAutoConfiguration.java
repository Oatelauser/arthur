package com.arthur.plugin.config;

import com.arthur.common.response.ServerResponse;
import com.arthur.plugin.result.ArthurResult;
import com.arthur.plugin.result.WebFluxResultFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * ArthurPlugin自动配置类
 *
 * @author DearYang
 * @date 2022-08-15
 * @since 1.0
 */
@AutoConfiguration
public class ArthurPluginAutoConfiguration {

    @Bean
    public WebFluxResultFactory<ServerResponse> webFluxResultFactory() {
        return ArthurResult::new;
    }

}
