package com.java.yxt.annotation;

import java.lang.annotation.*;

/**
 * 设置创建人注解
 * @author zanglei
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SetCreaterName {
}
