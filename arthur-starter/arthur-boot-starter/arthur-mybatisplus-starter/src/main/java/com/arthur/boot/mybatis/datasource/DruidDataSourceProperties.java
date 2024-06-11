package com.arthur.boot.mybatis.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Druid多数据源配置
 *
 * @author DearYang
 * @date 2022-09-22
 * @since 1.0
 */
@SuppressWarnings("unused")
@ConfigurationProperties(prefix = "spring.datasource.dynamic.druid")
public class DruidDataSourceProperties extends DynamicDataSourceValueSupport {

	private Map<String, DruidDataSource> dataSources;

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if (CollectionUtils.isEmpty(dataSources)) {
			throw new IllegalArgumentException("Property [spring.datasource.dynamic.druid] is empty");
		}

		for (Map.Entry<String, DruidDataSource> entry : dataSources.entrySet()) {
			DruidDataSource druid = entry.getValue();
			if (druid.getUrl() == null) {
				throw new IllegalArgumentException("dynamic database url can not be null");
			}
			if (druid.getUrl().equals(basicProperties.determineUrl())) {
				throw new IllegalArgumentException("dynamic database url can not equals: " + basicProperties.determineUrl() + ", " +
					"because original url already set");
			}

			if (druid.getUsername() == null) {
				druid.setUsername(basicProperties.determineUsername());
			}
			if (druid.getPassword() == null) {
				druid.setPassword(basicProperties.determinePassword());
			}
			if (druid.getDriverClassName() == null) {
				druid.setDriverClassName(basicProperties.getDriverClassName());
			}
			// 初始化
			druid.init();
		}
	}

	@Override
	public Map<String, DataSource> loadDynamicDataSources() {
		return new HashMap<>(dataSources);
	}

	public void setDataSources(Map<String, DruidDataSource> dataSources) {
		this.dataSources = dataSources;
	}

}
