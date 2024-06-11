package com.arthur.boot.constant;

import com.arthur.boot.core.url.CachingUrlPathMather;
import com.arthur.boot.lifecycle.AbstractServiceInitializer;
import com.arthur.boot.process.AwareBeanPostProcessor;

/**
 * 常量
 *
 * @author DearYang
 * @date 2022-08-01
 * @since 1.0
 */
public interface BootConstants {

    /**
     * copy by {@code org.springframework.cloud.bootstrap.BootstrapApplicationListener#BOOTSTRAP_PROPERTY_SOURCE_NAME}
     *
     * @see AbstractServiceInitializer
     */
    String BOOTSTRAP_PROPERTY_SOURCE_NAME = "bootstrap";

    /**
     * 校验aware的方法名的前缀
     *
     * @see AwareBeanPostProcessor
     */
    String AWARE_METHOD_PREFIX = "set";

    /**
     * 缓存url的分割符
     *
     * @see CachingUrlPathMather
     */
    String CACHE_URL_DELIMITER = "@";

	/**
	 * Spring基础包名
	 */
	String SPRING_BASE_PACKAGE = "org.springframework";

}
