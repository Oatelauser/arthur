package com.arthur.web.servlet.autoconfigure;

import com.arthur.web.servlet.constant.WebMvcConstants;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.http.conn.ssl.SSLConnectionSocketFactory.TLS;


/**
 * RestTemplate 请求模板配置
 *
 * @author DearYang
 * @date 2022-09-09
 * @see RestTemplate
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RestTemplate.class, HttpClient.class})
public class WebRestTemplateConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(WebRestTemplateConfiguration.class);
	private static final X509TrustManager X509 = new NoopTrustManager();

	private final HttpClientPoolProperties poolProperties;

	public WebRestTemplateConfiguration(HttpClientPoolProperties poolProperties) {
		this.poolProperties = poolProperties;
	}

	@Bean(name = WebMvcConstants.REST_TEMPLATE_BEAN)
	public RestTemplate httpRestTemplate(ClientHttpRequestFactory requestFactory) {
		return this.createRestTemplate(requestFactory);
	}

	@Bean
	public ClientHttpRequestFactory clientHttpRequestFactory() {
		// maxTotalConnection 和 maxConnectionPerRoute 必须要配
		if (poolProperties.getMaxTotalConnect() <= 0) {
			throw new IllegalArgumentException("invalid maxTotalConnection: " + poolProperties.getMaxTotalConnect());
		}
		if (poolProperties.getMaxConnectPerRoute() <= 0) {
			throw new IllegalArgumentException("invalid maxConnectionPerRoute: " + poolProperties.getMaxConnectPerRoute());
		}
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(Objects.requireNonNull(httpClient()));
		// 连接超时
		clientHttpRequestFactory.setConnectTimeout(poolProperties.getConnectTimeout());
		// 从连接池获取请求连接的超时时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
		clientHttpRequestFactory.setConnectionRequestTimeout(poolProperties.getConnectionRequestTimout());
		return clientHttpRequestFactory;
	}

	private HttpClient httpClient() {
		try {
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
			//设置信任ssl访问
			//SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();

			SSLContext sslContext = SSLContext.getInstance(TLS);
			sslContext.init(null, new TrustManager[]{X509}, null);
			//httpClientBuilder.setSSLContext(sslContext);
			// 允许所有SSL证书
			HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);

			// 注册http和https请求
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", sslConnectionSocketFactory)
				.build();

			//使用Httpclient连接池的方式配置(推荐)，同时支持netty，okHttp以及其他http框架
			PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			// 最大连接数
			poolingHttpClientConnectionManager.setMaxTotal(poolProperties.getMaxTotalConnect());
			// 同路由并发数
			poolingHttpClientConnectionManager.setDefaultMaxPerRoute(poolProperties.getMaxConnectPerRoute());

			// 数据读取超时时间，即SocketTimeout
			SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(Timeout.ofMilliseconds(poolProperties.getReadTimeout())).build();
			poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);
			// 配置连接池
			httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
			// 重试次数
			int retryTimes = poolProperties.getRetryTimes();
			if (retryTimes > 0) {
				httpClientBuilder.setRetryStrategy(new DefaultHttpRequestRetryStrategy(poolProperties.getRetryTimes(), TimeValue.ofSeconds(1L)));
				//httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(PoolProperties.getRetryTimes(), true));
			}

			// 设置默认请求头
			List<Header> headers = this.getDefaultHeaders();
			httpClientBuilder.setDefaultHeaders(headers);
			// 设置长连接保持策略
			//httpClientBuilder.setKeepAliveStrategy(connectionKeepAliveStrategy());

			// 设置后台线程剔除失效连接
			httpClientBuilder.evictExpiredConnections();
			httpClientBuilder.evictIdleConnections(TimeValue.ofSeconds(poolProperties.getKeepAliveTime()));

			return httpClientBuilder.build();
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			LOG.error("Initializing HTTP connection pool exception", e);
		}
		return null;
	}

	private List<Header> getDefaultHeaders() {
		List<Header> headers = new ArrayList<>();
		headers.add(new BasicHeader("User-Agent",
			"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
		headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
		headers.add(new BasicHeader("Accept-Language", "zh-CN"));
		headers.add(new BasicHeader("Connection", "Keep-Alive"));
		return headers;
	}

	///**
	// * 配置长连接保持策略
	// *
	// * @return 策略
	// */
	//public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
	//	return (response, context) -> {
	//		// Honor 'keep-alive' header
	//		BasicHeaderElementIterator it = new BasicHeaderElementIterator(
	//			response.headerIterator(HTTP.CONN_KEEP_ALIVE));
	//		while (it.hasNext()) {
	//			HeaderElement he = it.next();
	//			if (LOG.isDebugEnabled()) {
	//				LOG.debug("HeaderElement:{}", he);
	//			}
	//			String param = he.getName();
	//			String value = he.getValue();
	//			if (value != null && "timeout".equalsIgnoreCase(param)) {
	//				try {
	//					return TimeValue.ofSeconds(Long.parseLong(value));
	//				} catch (NumberFormatException e) {
	//					LOG.error("Parse long connection expiration time exception", e);
	//				}
	//			}
	//		}
	//		HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
	//		//如果请求目标地址,单独配置了长连接保持时间,使用该配置
	//		Optional<Map.Entry<String, Integer>> any = Optional.ofNullable(poolProperties.getKeepAliveTargetHost())
	//			.orElseGet(HashMap::new)
	//			.entrySet().stream().filter(
	//				e -> e.getKey().equalsIgnoreCase(target.getHostName())).findAny();
	//		// 否则使用默认长连接保持时间
	//		return any.map(en -> TimeValue.ofSeconds(en.getValue())).orElse(TimeValue.ofSeconds(poolProperties.getKeepAliveTime()));
	//	};
	//}


	private RestTemplate createRestTemplate(ClientHttpRequestFactory factory) {
		RestTemplate restTemplate = new RestTemplate(factory);
		modifyDefaultCharset(restTemplate);
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
		return restTemplate;
	}

	private void modifyDefaultCharset(RestTemplate restTemplate) {
		List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
		HttpMessageConverter<?> converterTarget = null;
		for (HttpMessageConverter<?> item : converterList) {
			if (StringHttpMessageConverter.class == item.getClass()) {
				converterTarget = item;
				break;
			}
		}
		if (null != converterTarget) {
			converterList.remove(converterTarget);
		}
		Charset defaultCharset = Charset.forName(poolProperties.getCharset());
		converterList.add(1, new StringHttpMessageConverter(defaultCharset));
	}

	/**
	 * 设置证书X509
	 */
	private static final class NoopTrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

}
