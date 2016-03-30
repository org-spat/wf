package org.spat.wf.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识Action所处理HTTP请求类型
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
	//有效时间 ，单位毫秒，默认60秒
	int server() default 60 * 1000;
	//本地缓存时间，0标示不缓存
	int client() default 0;
	//匹配url
	String pattern() default "";
	//忽略的参数,空代表不忽略任何参数，*代表忽略所有参数
	String igParams() default "";
}

