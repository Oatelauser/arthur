package com.arthur.oauth2.constant;

import com.arthur.oauth2.authentication.OAuth2AnnotationAnalysisProcessor;
import com.arthur.oauth2.resource.ArthurBearerTokenResolver;
import com.arthur.oauth2.server.RedisOAuth2AuthorizationConsentService;
import com.arthur.oauth2.server.RedisOAuth2AuthorizationService;

import java.time.Duration;
import java.util.regex.Pattern;

/**
 * OAuth2常量
 *
 * @author DearYang
 * @date 2022-08-15
 * @since 1.0
 */
public interface OAuth2Constants {

	/**
	 * path variable匹配
	 *
	 * @see OAuth2AnnotationAnalysisProcessor
	 */
	Pattern PATH_VARIABLE_PATTERN = Pattern.compile("\\{(.*?)}");

	/**
	 * path variable数据值替换
	 *
	 * @see OAuth2AnnotationAnalysisProcessor
	 */
	String PATH_VARIABLE_REPLACEMENT = "*";

	/**
	 * Token的前缀
	 *
	 * @see ArthurBearerTokenResolver
	 */
	String AUTHORIZATION_TOKEN_PREFIX = "bearer";

	/**
	 * Token正则匹配的分组名
	 *
	 * @see OAuth2Constants#AUTHORIZATION_PATTERN
	 */
	String AUTHORIZATION_PATTERN_GROUP = "token";

	/**
	 * Token正则匹配
	 *
	 * @see ArthurBearerTokenResolver
	 */
	Pattern AUTHORIZATION_PATTERN = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$", Pattern.CASE_INSENSITIVE);

	/**
	 * 表单请求方式Token的参数名
	 *
	 * @see ArthurBearerTokenResolver
	 */
	String AUTHORIZATION_TOKEN_PARAMETER_NAME = "access_token";

	/**
	 * 令牌刷新有效时间，默认30天
	 */
	Duration REFRESH_TOKEN_LIVE_TIME_SECONDS = Duration.ofSeconds(60 * 60 * 24 * 30);

	/**
	 * 请求令牌有效期，默认12小时
	 */
	Duration TOKE_LIVE_TIME_SECONDS = Duration.ofSeconds(60 * 60 * 12);

	/**
	 * OAuth2无效请求异常文档URL
	 */
	String OAUTH2_INVALID_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	/**
	 * OAuth2服务异常文档URL
	 */
	String OAUTH2_SERVER_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";

	/**
	 * 设置AOP Advice的Bean名称
	 */
	String DEFAULT_ADVICE_BEAN_NAME = "authPermissionAdvisor";

	/**
	 * redis key的公共前缀
	 *
	 * @see RedisOAuth2AuthorizationService
	 * @see RedisOAuth2AuthorizationConsentService
	 */
	String TOKEN_KEY_PREFIX = "token";

	/**
	 * redis key的过期时间
	 *
	 * @see RedisOAuth2AuthorizationService
	 * @see RedisOAuth2AuthorizationConsentService
	 */
	Duration DEFAULT_TIMEOUT = Duration.ofMinutes(10);

}
