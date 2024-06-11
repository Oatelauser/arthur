package com.arthur.boot.mybatis.core;

import com.alibaba.druid.DbType;
import com.alibaba.druid.util.JdbcUtils;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.util.Assert;

import java.util.List;

/**
 * MySQL 高性能批量插入注射器
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-01-05
 * @see InsertBatchSomeColumn
 * @see GenericMapper
 * @since 1.0
 */
public class MysqlBatchSqlInjector extends DefaultSqlInjector implements InitializingBean {

	private final DataSourceProperties dataSourceProperties;

	public MysqlBatchSqlInjector(DataSourceProperties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}

	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
		List<AbstractMethod> methods = super.getMethodList(mapperClass, tableInfo);
		methods.add(new InsertBatchSomeColumn(t -> t.getFieldFill() != FieldFill.INSERT_UPDATE));
		return methods;
	}

	@Override
	public void afterPropertiesSet() {
		DbType dbType = JdbcUtils.getDbTypeRaw(dataSourceProperties.determineUrl(),
			dataSourceProperties.determineDriverClassName());
		Assert.state(dbType == null || DbType.mysql.equals(dbType), "必须在MySQL使用");
	}

}
