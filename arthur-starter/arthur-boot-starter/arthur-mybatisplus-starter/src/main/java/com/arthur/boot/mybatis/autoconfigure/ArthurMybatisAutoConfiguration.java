package com.arthur.boot.mybatis.autoconfigure;

import com.arthur.boot.conditional.ConditionalOnDbType;
import com.arthur.boot.mybatis.core.MysqlBatchSqlInjector;
import com.arthur.boot.mybatis.handler.MybatisPlusMetaObjectHandler;
import com.arthur.boot.mybatis.plugin.ExtendingPaginationInnerInterceptor;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;

import static com.arthur.boot.conditional.ConditionalOnDbType.DbType.mysql;

/**
 * MybatisPlus自动配置类
 *
 * @author DearYang
 * @date 2022-08-14
 * @since 1.0
 */
@AutoConfiguration
public class ArthurMybatisAutoConfiguration {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new ExtendingPaginationInnerInterceptor());
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    @Bean
    public MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler() {
        return new MybatisPlusMetaObjectHandler();
    }

	@Bean
	@ConditionalOnDbType(mysql)
	public ISqlInjector mysqlSqlInjector(DataSourceProperties dataSourceProperties) {
		return new MysqlBatchSqlInjector(dataSourceProperties);
	}

}
