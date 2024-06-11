package com.arthur.boot.mybatis.datasource;

import com.arthur.boot.mybatis.annotation.EnableMultiDatabase;
import com.arthur.boot.mybatis.autoconfigure.DynamicDataSourceConfiguration;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

/**
 * 开启动态数据源的选择器
 *
 * @author DearYang
 * @date 2022-10-06
 * @since 1.0
 */
public class DatabaseConfigurationSelector extends AdviceModeImportSelector<EnableMultiDatabase> {

	@Override
	protected String[] selectImports(AdviceMode adviceMode) {
		return switch (adviceMode) {
			case PROXY -> new String[]{ DynamicDataSourceConfiguration.class.getName() };
			// 不支持AspectJ
			case ASPECTJ -> null;
		};
	}

}
