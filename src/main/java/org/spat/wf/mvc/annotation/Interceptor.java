package org.spat.wf.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.spat.wf.mvc.WFInterceptor;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Interceptor {
    
    int order() default 0; //顺序号
    
    Class<? extends WFInterceptor> interceptor()  ;

    String methed() default "";//需要拦截的HTTP方法，默认全部
}