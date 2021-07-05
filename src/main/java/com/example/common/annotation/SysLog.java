package com.example.common.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @author dj
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	String value() default "";
}
