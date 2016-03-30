package org.spat.wf.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Interceptor {
	
    String path() default ""; //路径匹配，支持正则表达式
    
    int order() default 0; //顺序号
    
    Position position() default Position.PRE_EXE; //拦截位置
	
	public enum Position {
		/**
		 * 作用于全局
		 */
		PRE_EXE,
		
		/**
		 * 作用于某个Action
		 */
		AFTER_EXE;
	}
}