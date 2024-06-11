package com.arthur.boot.mybatis.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.arthur.boot.mybatis.annotation.EnableMultiDatabase;
import com.arthur.boot.mybatis.datasource.*;
import com.baomidou.mybatisplus.autoconfigure.SqlSessionFactoryBeanCustomizer;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import static com.arthur.boot.mybatis.constant.MybatisConstants.DEFAULT_DATASOURCE_NAME;
import static org.springframework.beans.factory.config.BeanDefinition.ROLE_INFRASTRUCTURE;

/**
 * 动态数据源自动配置类
 *
 * @author DearYang
 * @date 2022-09-22
 * @since 1.0
 */
@SuppressWarnings("all")
@Configuration(proxyBeanMethods = false)
public class DynamicDataSourceConfiguration {

	@Configuration(proxyBeanMethods = false)
	@Import({DruidDataSourceConfiguration.class, HikariDataSourceConfiguration.class, AnnotationDataSourceConfiguration.class})
	static class DataSourceConfiguration {
	}

	@ConditionalOnBean(DataSourceProvider.class)
	@AutoConfigureAfter(AnnotationDataSourceConfiguration.class)
	static class AnnotationDataSourceConfiguration implements ImportAware {

		private AnnotationAttributes enableDataSource;

		@Bean
		@ConditionalOnMissingBean
		DelegateRoutingDataSource dynamicDataSource(RouteLookup lookupKey, DataSourceProvider dataSourceProvider,
													ObjectProvider<DataSource> provider) {
			DynamicDataSourceRoute dataSourceRouter = new DynamicDataSourceRoute(lookupKey);
			Map<Object, Object> targetDataSources = new HashMap<>(dataSourceProvider.dataSources);
			dataSourceRouter.setTargetDataSources(targetDataSources);

			// 设置默认数据源
			DataSource defaultDataSource = this.selectDefaultDataSource(dataSourceProvider, provider);
			Assert.notNull(defaultDataSource, "Can not find DataSource object");
			dataSourceRouter.setDefaultTargetDataSource(defaultDataSource);

			return new DelegateRoutingDataSource(dataSourceRouter);
		}

		@Bean
		@ConditionalOnMissingBean
		RouteLookup defaultRouteLookupKey() {
			return new ThreadLocalLookup();
		}

		//@Bean
		//@Role(ROLE_INFRASTRUCTURE)
		//@ConditionalOnBean(RouteLookup.class)
		//Advisor dynamicDataSourceAdvice(RouteLookup lookupKey, CacheManager cacheManager) {
		//	return new AnnotationDatabaseAdvisor(lookupKey, cacheManager);
		//}

		@Bean
		@Role(ROLE_INFRASTRUCTURE)
		@ConditionalOnBean(RouteLookup.class)
        DatabaseAnnotationBeanPostProcessor dynamicDataSourceAdvice(RouteLookup lookupKey, CacheManager cacheManager) {
			Assert.notNull(this.enableDataSource, "@EnableDataSource annotation metadata was not injected");
			DatabaseAnnotationBeanPostProcessor processor = new DatabaseAnnotationBeanPostProcessor(lookupKey, cacheManager);
			Class<? extends Annotation> customAnnotation = this.enableDataSource.getClass("annotation");
			if (customAnnotation != AnnotationUtils.getDefaultValue(EnableMultiDatabase.class, "annotation")) {
				processor.setAnnotationType(customAnnotation);
			}

			processor.setProxyTargetClass(this.enableDataSource.getBoolean("proxyTargetClass"));
			processor.setOrder(this.enableDataSource.getNumber("order"));
			return processor;
		}

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnBean(DelegateRoutingDataSource.class)
		SqlSessionFactoryBeanCustomizer sqlSessionFactoryBeanCustomizer(DelegateRoutingDataSource provider) {
			return factoryBean -> factoryBean.setDataSource(provider.delegate);
		}

		/**
		 * 选择默认的数据源
		 * <p>
		 * 选择优先级：
		 * 1.存在DataSource Bean对象
		 * 3.获取{@link DataSourceProvider#dataSources}中values的第一个
		 *
		 * @param provider {@link ObjectProvider}
		 * @return 数据源
		 */
		private DataSource selectDefaultDataSource(DataSourceProvider dataSourceProvider, ObjectProvider<DataSource> provider) {
			DataSource defaultDataSource = provider.getIfAvailable();
			if (defaultDataSource != null) {
				return defaultDataSource;
			}

			defaultDataSource = dataSourceProvider.dataSources.get(DEFAULT_DATASOURCE_NAME);
			if (defaultDataSource != null) {
				return defaultDataSource;
			}

			DataSource[] dataSources = dataSourceProvider.dataSources.values().toArray(new DataSource[0]);
			return dataSources[0];
		}

		@Override
		public void setImportMetadata(AnnotationMetadata importMetadata) {
			AnnotationAttributes enableDataSource = AnnotationAttributes.fromMap(
				importMetadata.getAnnotationAttributes(EnableMultiDatabase.class.getName()));
			if (enableDataSource == null) {
				throw new IllegalArgumentException("@EnableDataSource is not present on importing class "
					+ importMetadata.getClassName());
			}
			this.enableDataSource = enableDataSource;
		}

	}

	@ConditionalOnClass(DruidDataSource.class)
	@EnableConfigurationProperties(DruidDataSourceProperties.class)
	static class DruidDataSourceConfiguration {

		@Bean
		@ConditionalOnMissingBean
		DataSourceProvider dynamicDataSources(DruidDataSourceProperties druidProperties) {
			return new DataSourceProvider(druidProperties.getDataDynamicSources());
		}

	}

	@ConditionalOnClass(HikariConfig.class)
	@EnableConfigurationProperties(HikariDataSourceProperties.class)
	@ConditionalOnMissingClass("com.alibaba.druid.pool.DruidDataSource")
	static class HikariDataSourceConfiguration {

		@Bean
		@ConditionalOnMissingBean
		DataSourceProvider dynamicDataSources(HikariDataSourceProperties hikariProperties) {
			return new DataSourceProvider(hikariProperties.getDataDynamicSources());
		}

	}

	static class DataSourceProvider {

		final Map<String, DataSource> dataSources;

		DataSourceProvider(Map<String, DataSource> dataSources) {
			this.dataSources = dataSources;
		}

	}

	static class DelegateRoutingDataSource implements InitializingBean {

		private final AbstractRoutingDataSource delegate;

		public DelegateRoutingDataSource(AbstractRoutingDataSource delegate) {
			this.delegate = delegate;
		}

		@Override
		public void afterPropertiesSet() {
			delegate.afterPropertiesSet();
		}
	}

}
