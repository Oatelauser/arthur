package com.arthur.boot.mybatis.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

/**
 * 动态数据源配置
 *
 * @author DearYang
 * @date 2022-09-22
 * @since 1.0
 */
public abstract class DynamicDataSourceValueSupport implements Closeable, InitializingBean, ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(DynamicDataSourceValueSupport.class);

	protected DataSourceProperties basicProperties;
	private volatile Map<String, DataSource> dataDynamicSources;

	public Map<String, DataSource> getDataDynamicSources() {
		if (dataDynamicSources == null) {
			synchronized (this) {
				if (dataDynamicSources == null) {
					dataDynamicSources = this.loadDynamicDataSources();
				}
			}
		}
		return dataDynamicSources;
	}

	@Override
	public void close() {
		if (CollectionUtils.isEmpty(dataDynamicSources)) {
			return;
		}

		for (DataSource dataSource : dataDynamicSources.values()) {
			if (dataSource instanceof Closeable) {
				try {
					((Closeable) dataSource).close();
				} catch (IOException e) {
					LOG.error("Close DataSource error", e);
				}
			}
		}
	}

	/**
	 * 加载动态数据源
	 *
	 * @return 动态数据源
	 */
	public abstract Map<String, DataSource> loadDynamicDataSources();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		basicProperties = applicationContext.getBean(DataSourceProperties.class);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(basicProperties, "Can not get bean object [DataSourceProperties]");
	}

}
