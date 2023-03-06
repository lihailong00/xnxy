package com.lee.xnxydev.aop;

import java.lang.annotation.*;

/**
 * @author 晓龙coding
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String module() default  "";

    String operator() default "";
}
