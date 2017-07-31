package com.salon.backstage.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能：持久对象的CURD操作
 * @author xiongliang
 *         mobile enterprise application platform Version 0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CRUD {
	boolean create() default true;

	boolean read() default true;

	boolean update() default true;

	boolean delete() default true;
}