package org.spat.wf.mvc.interceptor;

import java.util.HashMap;

import org.spat.wf.mvc.WFInterceptor;
import org.spat.wf.mvc.annotation.Interceptor;

public class InterceptorLoader {
	private static HashMap<Class<WFInterceptor>, WFInterceptor> Interceptors =new HashMap<Class<WFInterceptor>, WFInterceptor>();
	
	@SuppressWarnings("unchecked")
	public static WFInterceptor LoadInterceptor(Interceptor interceptor) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		Class<WFInterceptor> clazz = (Class<WFInterceptor>) interceptor.interceptor();
		if(clazz==null){
			throw new ClassNotFoundException();
		}
		WFInterceptor instance = Interceptors.get(clazz);
		if(instance==null){
			instance = clazz.newInstance();
			instance.setOrder(interceptor.order());
			Interceptors.put(clazz, instance);
		}
		return instance;
	}
}
