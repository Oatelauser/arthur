package com.arthur.common.spi;

import java.lang.annotation.*;

/**
 * copy by dubbo SPI
 * <a href="https://github.com/apache/dubbo/blob/master/dubbo-common/src/main/java/org/apache/dubbo/common/extension">Apache Dubbo Common Extension</a>.
 *
 * @author dubbo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    /**
     * default extension name
     */
    String value() default "";

}
