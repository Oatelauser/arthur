package com.arthur.web.servlet.autoconfigure;

import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.MimeMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * Undertow静态资源配置
 * <p>
 * 设置静态资源编码为UTF-8，防止中文乱码
 *
 * @author DearYang
 * @date 2022-10-11
 * @see <a href="https://springboot.io/t/topic/4831">UndertowConfiguration</a>
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DeploymentInfo.class)
public class UndertowStaticResourceConfiguration implements UndertowDeploymentInfoCustomizer {

	/**
	 * 覆盖默认文本类型的字符ContentType， 添加UTF8编码
	 *
	 * @see io.undertow.util.MimeMappings
	 */
	@Override
	public void customize(DeploymentInfo deploymentInfo) {
		String charset = "charset=UTF-8";
		deploymentInfo.addMimeMapping(new MimeMapping("html", "text/html;" + charset));
		deploymentInfo.addMimeMapping(new MimeMapping("htm", "text/html;" + charset));
		deploymentInfo.addMimeMapping(new MimeMapping("css", "text/css;" + charset));
		deploymentInfo.addMimeMapping(new MimeMapping("js", "application/javascript;" + charset));
		deploymentInfo.addMimeMapping(new MimeMapping("json", "application/json;" + charset));
		deploymentInfo.addMimeMapping(new MimeMapping("md", "text/markdown;" + charset));
	}

}
