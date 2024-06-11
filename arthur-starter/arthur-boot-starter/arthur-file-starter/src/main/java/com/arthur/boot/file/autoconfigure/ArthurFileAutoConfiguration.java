package com.arthur.boot.file.autoconfigure;

import com.alibaba.excel.EasyExcel;
import com.arthur.boot.conditional.ConditionalOnMissingReactive;
import com.arthur.boot.file.core.ExceptionHandler;
import com.arthur.boot.file.core.HttpFileDownloader;
import com.arthur.boot.file.excel.ExcelRowConverter;
import com.arthur.boot.file.handler.ExcelMappingHandlerMapping;
import com.arthur.boot.file.handler.ExcelResponseMethodHandler;
import com.arthur.boot.file.reslover.HandlerMethodExcelParameterResolver;
import com.arthur.boot.file.reslover.ServletExcelResponseMethodArgumentResolver;
import com.arthur.web.servlet.process.SpringMappingHandlerRegistar;
import com.arthur.web.servlet.server.WebServiceResponse;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

/**
 * Excel自动配置类
 *
 * @author DearYang
 * @date 2022-09-28
 * @since 1.0
 */
@ConditionalOnMissingReactive
@AutoConfiguration(after = WebMvcAutoConfiguration.class)
@ConditionalOnProperty(value = "arthur.file.enabled", havingValue = "true", matchIfMissing = true)
public class ArthurFileAutoConfiguration {

	@ConditionalOnClass(EasyExcel.class)
	@Configuration(proxyBeanMethods = false)
	//@ConditionalOnProperty(value = "arthur.excel.easy.enabled", havingValue = "true")
	static class EasyExcelConfiguration {

		@Bean
		@ConditionalOnMissingBean
		ExcelRowConverter excelRowConverter() {
			return new ExcelRowConverter();
		}

		@Bean
		@ConditionalOnMissingBean
        ExcelResponseMethodHandler excelResponseMethodHandler(WebServiceResponse webServiceResponse) {
			return new ExcelResponseMethodHandler(webServiceResponse);
		}

		@Bean
		@ConditionalOnMissingBean
        HandlerMethodExcelParameterResolver handlerMethodExcelParameterResolver(ConversionService conversionService) {
			return new HandlerMethodExcelParameterResolver(conversionService);
		}

		@Bean
		@ConditionalOnMissingBean
        ServletExcelResponseMethodArgumentResolver excelResponseMethodArgumentResolver() {
			return new ServletExcelResponseMethodArgumentResolver();
		}

		@Bean
		@ConditionalOnMissingBean
		ExcelMappingHandlerMapping excelMappingHandlerMapping(SpringMappingHandlerRegistar handlerMappingRegistar) {
			return new ExcelMappingHandlerMapping(handlerMappingRegistar);
		}

	}

	static class WebFileConfiguration {

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnBean(ExceptionHandler.class)
        HttpFileDownloader fileDownloader(ExceptionHandler exceptionHandler) {
			return new HttpFileDownloader(exceptionHandler);
		}

	}

}
