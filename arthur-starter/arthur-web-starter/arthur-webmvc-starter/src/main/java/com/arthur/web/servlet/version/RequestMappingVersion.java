package com.arthur.web.servlet.version;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API版本控制注解
 *
 * @author DearYang
 * @date 2022-10-12
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequestMappingVersion {

    /**
     * @return 版本号
     */
    @AliasFor("version")
    int value() default 1;

    /**
     * @return 版本号
     */
    @AliasFor("value")
    int version() default 1;

    /**
     * 忽略版本号中的前缀字符串，比如：v1 -> 1
     */
    String prefix() default "v";

    /**
     * 版本匹配规则
     */
    Version conditionType() default Version.EQUAL;

    enum Version {
        /**
         * 等于
         */
        EQUAL,

        /**
         * 大于
         */
        GT,

        /**
         * 大于等于
         */
        GTE,

        /**
         * 小于
         */
        LT,

        /**
         * 小于等于
         */
        LTE,
    }

}
