package org.spat.wf.mvc.initial.sys;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.spat.wf.mvc.WFConfig;
import org.spat.wf.mvc.WFInterceptor;
import org.spat.wf.mvc.annotation.Interceptor;
import org.spat.wf.mvc.annotation.Interceptor.Position;
import org.spat.wf.mvc.interceptor.InterceptorWrap;
import org.spat.wf.utils.ClassUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class InterceptorInit {

	private static List<InterceptorWrap> globalInterceptors;
	
	public static List<InterceptorWrap> getGlobalInterceptors() {
		
		return globalInterceptors;
	}
	
	public  static void init() throws Exception {
		buildGlobalInterceptors() ;
	}

    private static void buildGlobalInterceptors() throws Exception {
    	
    	Set<Class<? extends WFInterceptor>> interceptorsClasses = parseInterceptors(WFConfig.Instance().getNamespace(),".*\\.interceptors\\..*Interceptor");
    	List<InterceptorWrap> interceptors = new ArrayList<InterceptorWrap>();
		for(Iterator<Class<? extends WFInterceptor>> it = 
				interceptorsClasses.iterator(); it.hasNext();){
			
			Class<? extends WFInterceptor> clazz = it.next();
			
			WFInterceptor interceptor = null;
			try {
				Interceptor annotation = clazz.getAnnotation(Interceptor.class);
				if(annotation!=null){
					interceptor = clazz.newInstance();
					InterceptorWrap iWrap = new InterceptorWrap(annotation.path(), annotation.order(), annotation.position(), interceptor);
					interceptors.add(iWrap);	
				}else{ //没有注解的按默认配置处理
					interceptor = clazz.newInstance();
					InterceptorWrap iWrap = new InterceptorWrap("", 0, Position.PRE_EXE, interceptor);
					interceptors.add(iWrap);
				}
				
			} catch (Exception e) {
				System.err.println("Build global interceptor failed, Interceptor: " + clazz.getName());
				throw new Exception("Build global interceptor failed!",e);
			}
			
		}
		// 根据order进行排序
        Collections.sort(interceptors, new InterceptorWrap.InterceptorComparator());
        for (InterceptorWrap interceptor : interceptors) {
        	System.out.println("Load Global Interceptor : " + interceptor.getInterceptor().getClass().getName());
        }
		globalInterceptors = ImmutableList.copyOf(interceptors);
	}

	@SuppressWarnings("unchecked")
	private static Set<Class<? extends WFInterceptor>> parseInterceptors(String packagePrefix, String interceptPattern) {
		Set<Class<?>> classSet = ClassUtils.getClasses(packagePrefix);

	    Pattern interceptorPattern = Pattern.compile(interceptPattern);


	    ImmutableSet.Builder<Class<? extends WFInterceptor>> builder = ImmutableSet.builder();

	    for (Class<?> clazz : classSet)
	        if (rules(clazz, interceptorPattern))
	            builder
	                .add((Class<? extends WFInterceptor>) clazz)
	                .build();

	    return builder.build();
	}
	
	private static boolean rules(Class<?> clazz, Pattern interceptorPattern) {

        return WFInterceptor.class.isAssignableFrom(clazz)
                && interceptorPattern.matcher(clazz.getName()).matches()
                && !Modifier.isInterface(clazz.getModifiers())
                && !Modifier.isAbstract(clazz.getModifiers())
                && Modifier.isPublic(clazz.getModifiers());
    }
        
}
