package com.arthur.web.autoconfigure;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Jackson 自动配置类
 *
 * @author DearYang
 * @date 2022-09-06
 * @see Jackson2ObjectMapperBuilderCustomizer
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ ObjectMapper.class, Jdk8Module.class, JavaTimeModule.class })
class JacksonConfiguration {

	@Bean
	Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

		return (builder -> builder.modules(simpleModule, new Jdk8Module(), new JavaTimeModule())
			.timeZone(TimeZone.getTimeZone("GMT+8"))
			.dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
			.failOnEmptyBeans(false)
			.failOnUnknownProperties(false)
			.featuresToEnable(JsonParser.Feature.ALLOW_SINGLE_QUOTES, JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature())
			.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
	}

}
