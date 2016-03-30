package org.spat.wf.mvc.cache;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.spat.wf.mvc.WFConfig;
import org.spat.wf.utils.ClassUtils;

public class CacheProvider {
	
	private static Cache CACHE_CACHE = null;
	public static Cache getCache() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		if(CACHE_CACHE==null){
			Class<?> cacheCls = ClassUtils.getClass(WFConfig.Instance().getCache().getType());
			Constructor<?> c = cacheCls.getConstructor(WFConfig.Cache.class);
			Cache cache = (Cache)c.newInstance(WFConfig.Instance().getCache());
			CACHE_CACHE = cache;
		}
		return CACHE_CACHE;
	}
}
