package com.arthur.web.antisamy.web;

import com.arthur.boot.core.url.UrlPathMather;
import com.arthur.web.antisamy.autoconfigure.AntiSamyProperties;
import com.arthur.web.antisamy.autoconfigure.AntiSamyProperties.AntiSamyRouteConfig;
import com.arthur.web.antisamy.context.AntiSamyPolicyService;
import com.arthur.web.antisamy.context.AntiSamyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import java.io.IOException;
import java.util.Map;

/**
 * AntiSamy Servlet Filter
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
public class HttpServletRequestAntiSamyFilter extends OncePerRequestFilter implements AntiSamyFilterSupport {

	private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

	private final UrlPathMather urlPathMather;
	private final AntiSamyProperties properties;
	private final AntiSamyPolicyService antiSamyService;

	public HttpServletRequestAntiSamyFilter(AntiSamyProperties properties,
			UrlPathMather urlPathMather,
			AntiSamyPolicyService antiSamyService) {
		this.properties = properties;
		this.urlPathMather = urlPathMather;
		this.antiSamyService = antiSamyService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain chain) throws ServletException, IOException {
		String lookupPath = URL_PATH_HELPER.getLookupPathForRequest(request);
		for (Map.Entry<String, AntiSamyRouteConfig> entry : properties.getRoutes().entrySet()) {
			String urlPattern = entry.getKey();
			AntiSamyRouteConfig config;
			if (urlPathMather.matches(urlPattern, lookupPath) && shouldIgnoreRequest((config = entry.getValue()), request)) {
				AntiSamyService antiSamyService = this.getAntiSamyService(config, this.antiSamyService);
				request = new AntiSamyCachedRequestWrapper(request, antiSamyService);
				break;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean shouldIgnoreRequest(AntiSamyRouteConfig config, HttpServletRequest request) {
		return ObjectUtils.containsElement(config.getMethod(), request.getMethod());
	}

}
