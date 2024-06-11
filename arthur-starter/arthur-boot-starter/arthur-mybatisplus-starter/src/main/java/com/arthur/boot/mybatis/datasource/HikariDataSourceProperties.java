package com.arthur.boot.mybatis.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Hikari多数据源配置
 *
 * @author DearYang
 * @date 2022-09-22
 * @since 1.0
 */
@SuppressWarnings("unused")
@ConfigurationProperties(prefix = "spring.datasource.dynamic.hikari")
public class HikariDataSourceProperties extends DynamicDataSourceValueSupport {

	private Map<String, HikariConfig> dataSources;

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if (CollectionUtils.isEmpty(dataSources)) {
			throw new IllegalArgumentException("Property [spring.datasource.dynamic.hikari] is empty");
		}

		// 设置默认配置
		for (Map.Entry<String, HikariConfig> entry : dataSources.entrySet()) {
			HikariConfig config = entry.getValue();
			if (config.getJdbcUrl() == null) {
				throw new IllegalArgumentException("dynamic database url can not be null");
			}
			if (config.getJdbcUrl().equals(basicProperties.determineUrl())) {
				throw new IllegalArgumentException("dynamic database url can not equals: " + basicProperties.determineUrl() + ", " +
					"because original url already set");
			}
			if (config.getUsername() == null) {
				config.setUsername(basicProperties.determineUsername());
			}
			if (config.getPassword() == null) {
				config.setPassword(basicProperties.determinePassword());
			}
			if (config.getDriverClassName() == null) {
				config.setDriverClassName(basicProperties.determineDriverClassName());
			}
		}
	}

	@Override
	public Map<String, DataSource> loadDynamicDataSources() {
		HashMap<String, DataSource> hikariDataSources = new HashMap<>(dataSources.size());
		dataSources.forEach((k, config) -> dataSources.put(k, new HikariDataSource(config)));
		return hikariDataSources;
	}

	public void setDataSources(Map<String, HikariConfig> dataSources) {
		this.dataSources = dataSources;
	}

}
