package org.springframework.boot.autoconfigure.data.redis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * redis多数据源配置
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-19
 * @since 1.0
 */
@ConfigurationProperties(prefix = "spring.redis")
public class RedisMultiProperties implements InitializingBean {

	private Map<String, RedisProperties> multi;

	public Map<String, RedisProperties> getMulti() {
		return multi;
	}

	public void setMulti(Map<String, RedisProperties> multi) {
		this.multi = multi;
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notEmpty(multi, "multi cannot be empty");
		Assert.noNullElements(multi.values(), "multi value cannot be empty");
	}

}
