package com.arthur.common.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * spi load same type to override strategy
 *
 * @author DearYang
 * @date 2022-07-15
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OverrideImplementor {

    /**
     * @return override class
     */
    Class<?> value();

}
