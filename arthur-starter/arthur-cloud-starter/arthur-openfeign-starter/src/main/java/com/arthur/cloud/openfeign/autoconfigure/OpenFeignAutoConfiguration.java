package com.arthur.cloud.openfeign.autoconfigure;

import com.alibaba.csp.sentinel.SphU;
import com.arthur.cloud.openfeign.component.HeaderRequestInterceptor;
import com.arthur.cloud.openfeign.component.OpenFeignErrorDecoder;
import com.arthur.cloud.openfeign.component.SpringFormExpandEncoder;
import com.arthur.cloud.openfeign.conditional.ConditionalOnEnabledInterceptor;
import com.arthur.cloud.openfeign.sentinel.SentinelFeignBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.concurrent.TimeUnit;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * Open Feign自动配置类
 *
 * @author DearYang
 * @date 2022-09-06
 * @since 1.0
 */
@ConditionalOnClass(Feign.class)
@AutoConfiguration(before = FeignAutoConfiguration.class)
public class OpenFeignAutoConfiguration {


	@Configuration(proxyBeanMethods = false)
	static class OpenFeignExpandConfiguration {

		@Bean
		@ConditionalOnClass({SpringFormEncoder.class, HttpMessageConverters.class})
		Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
			return new SpringFormExpandEncoder(messageConverters);
		}

		/**
		 * 解决OpenFeign的继承性问题
		 */
		@Bean
		WebMvcRegistrations feignWebRegistrations() {
			return new WebMvcRegistrations() {
				@Override
				public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
					return new RequestMappingHandlerMapping() {
						@Override
						protected boolean isHandler(@NonNull Class<?> beanType) {
							return super.isHandler(beanType) &&
								!AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class);
						}
					};
				}
			};
		}

		/**
		 * Sentinel Feign降级自动配置类
		 */
		@ConditionalOnClass(SphU.class)
		@Configuration(proxyBeanMethods = false)
		@EnableConfigurationProperties(FallbackProperties.class)
		@ConditionalOnProperty(value = "feign.sentinel.enabled", havingValue = "true")
		static class FallbackConfiguration {

			@Bean
			@Primary
			@Scope(SCOPE_PROTOTYPE)
			Feign.Builder sentinelFeignBuilder(FallbackProperties fallbackProperties) {
				return new SentinelFeignBuilder(fallbackProperties);
			}

		}

		@Configuration(proxyBeanMethods = false)
		static class OpenFeignConfiguration {

			/**
			 * 1.NONE：默认级别，不记录任何信息
			 * 2.BASIC（适用于生产环境）：仅记录请求方法、URL、响应码、执行时间
			 * 3.HEADERS：基于BASIC的基础，记录请求、响应的头信息
			 * 4.FULL：在HEADERS的基础，记录body、元数据信息
			 *
			 * @return 日志级别
			 */
			@Bean
			public Logger.Level loggerLevel() {
				return Logger.Level.BASIC;
			}

			/**
			 * FeignClient超时设置
			 * feign超时设置有3种方式：配置文件直接配置FeignClient、自定义Request.Options及配置文件配置Ribbon，优先级从高到低如下。
			 * 1、配置文件里对特定FeignClient配置属性： feign.client.config.demo.connectTimeout=1000，feign.client.config.demo.readTimeout=2000 ；
			 * 2、自定义对特定FeignClient生效的Request.Options类型的Bean；
			 * 3、配置文件里对所有FeignClient属性的配置：feign.client.config.default.connectTimeout=1000，feign.client.config.default.readTimeout=5000
			 * 4、对全体FeignClient生效的Request.Options类型的Bean；
			 * 5、特定服务的ribbon配置：demo.ribbon.ConnectTimeout=1000，demo.ribbon.ReadTimeout=5000
			 * 6、全体服务的ribbon配置：ribbon.ConnectTimeout=1000，ribbon.ReadTimeout=5000
			 * 7、Ribbon默认配置：默认连接超时和读取超时都是1000，即1秒
			 * <p>
			 * 总结一下：
			 * 1、FeignClient的直接配置高于Ribbon的配置
			 * 2、特定服务的配置高于全体服务的配置
			 * 3、配置文件的配置高于自定义Request.Options
			 * 4、如果有特定服务的Options和全体服务的配置文件配置，遵循第二条规则，以特定服务的Options为准；
			 * 5、如果有特性服务的Ribbon配置和全体服务的FeignClient配置，遵循第一条规则，以FeingClient的配置为准
			 * <p>
			 * 最佳实践：
			 * 1、不要采用Ribbon配置而要直接配置FeignClient，即配置feign.client.xx
			 * 2、配置文件配置全体FeignClient的超时设置，同时对特定服务有特殊设置的，也在配置文件里配置
			 * <p>
			 *
			 * @see <a href="https://blog.csdn.net/weixin_36244726/article/details/103953852"></a>
			 */
			@Bean
			public Request.Options requestOptions() {
				return new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, false);
			}

			@Bean
			public ErrorDecoder openFeignErrorDecoder(ObjectMapper objectMapper) {
				return new OpenFeignErrorDecoder(objectMapper);
			}

			@Bean
			@ConditionalOnEnabledInterceptor
			public HeaderRequestInterceptor httpHeaderRequestInterceptor() {
				return new HeaderRequestInterceptor();
			}

		}
	}

}
