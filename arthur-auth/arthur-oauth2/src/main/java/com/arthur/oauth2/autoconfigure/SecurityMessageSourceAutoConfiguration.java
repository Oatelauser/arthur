package com.arthur.oauth2.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

import static com.arthur.oauth2.constant.SecurityConstants.MESSAGE_SOURCE_BEAN_NAME;

/**
 * 自定义认证异常信息国际化处理
 * <p>
 * 覆盖{@code org/springframework/security/messages}内置异常
 *
 * @author DearYang
 * @date 2022-08-24
 * @since 1.0
 */
@AutoConfiguration
public class SecurityMessageSourceAutoConfiguration {

    @Bean(name = MESSAGE_SOURCE_BEAN_NAME)
    public MessageSource authenticationMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:i18n/errors/messages");
        messageSource.setDefaultLocale(Locale.CHINA);
        return messageSource;
    }

	@Bean
	public MessageSourceAccessor messageSourceAccessor() {
		return new MessageSourceAccessor(authenticationMessageSource(), LocaleContextHolder.getLocale());
	}

}
