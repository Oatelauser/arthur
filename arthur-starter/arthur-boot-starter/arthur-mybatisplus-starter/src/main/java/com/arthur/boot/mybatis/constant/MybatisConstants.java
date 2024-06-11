package com.arthur.boot.mybatis.constant;

import com.arthur.boot.mybatis.annotation.DS;

/**
 * Mybatis常量
 *
 * @author DearYang
 * @date 2022-09-23
 * @since 1.0
 */
public interface MybatisConstants {

	/**
	 * 多数据源配置前缀
	 */
	String DYNAMIC_PREFIX_NAME = "spring.datasource.dynamic.enabled";

	/**
	 * 默认数据源在多数据源Map中的Key
	 */
	String DEFAULT_DATASOURCE_NAME = "master";

	/**
	 * 动态数据源缓存key
	 */
	String ROUTE_KEY_CACHE_NAME = "mybatis.dynamic.database";

	/**
	 * @see DS#name()
	 */
	String ANNOTATION_NAME = "name";

	/**
	 * @see DS#lookupName()
	 */
	String ANNOTATION_LOOK_UP_NAME = "lookupName";

}
