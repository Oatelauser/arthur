package com.arthur.web.antisamy.support;

import com.arthur.web.antisamy.annotation.AntiSamy;
import com.arthur.web.antisamy.autoconfigure.AntiSamyProperties;
import com.arthur.web.antisamy.autoconfigure.AntiSamyProperties.AntiSamyRouteConfig;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

/**
 * {@link AntiSamy}注解处理器设置AntiSamy防御规则
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-08
 * @since 1.0
 */
public class AntiSamyAnnotationHandler implements AnnotationHandler {

	private final AntiSamyProperties properties;

	public AntiSamyAnnotationHandler(AntiSamyProperties properties) {
		this.properties = properties;
	}

	@Override
	public void handleAnnotation(Set<String> urls, AntiSamy antiSamy) {
		for (String url : urls) {
			this.configureAntiSamyProperties(url, antiSamy);
		}
	}

	private void configureAntiSamyProperties(String url, AntiSamy antiSamy) {
		AntiSamyRouteConfig config = new AntiSamyRouteConfig();
		config.setDefenseHeaders(antiSamy.defenseHeaders());
		config.setDefenseQueryBody(antiSamy.defenseQueryBody());
		config.setDefenseCookies(antiSamy.defenseCookies());

		RequestMethod[] requestMethods = antiSamy.method();
		String[] methods = new String[requestMethods.length];
		for (int i = 0; i < requestMethods.length; i++) {
			methods[i] = requestMethods[i].name();
		}
		config.setMethod(methods);
		properties.getRoutes().put(url, config);
	}

}
