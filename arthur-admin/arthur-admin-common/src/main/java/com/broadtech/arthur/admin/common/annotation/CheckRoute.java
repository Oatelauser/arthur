package com.broadtech.arthur.admin.common.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * @author Machenike
 */
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRoute {
    String[] filters() default {};
    String[] predicates() default {};
}
