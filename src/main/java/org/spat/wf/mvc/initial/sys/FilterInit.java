package org.spat.wf.mvc.initial.sys;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.spat.wf.mvc.WFConfig;
import org.spat.wf.mvc.WFFilter;
import org.spat.wf.mvc.annotation.Filter;
import org.spat.wf.mvc.annotation.Filter.Position;
import org.spat.wf.mvc.filter.FiterWrap;
import org.spat.wf.utils.ClassUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class FilterInit {

	private static List<FiterWrap> globalFilters;
	
	public static List<FiterWrap> getGlobalFilters() {
		
		return globalFilters;
	}
	
	public  static void init() throws Exception {
		buildGlobalFilters() ;
	}

    private static void buildGlobalFilters() throws Exception {
    	System.out.println("Filter:path:.*\\.filters\\..*Filter");
    	Set<Class<? extends WFFilter>> interceptorsClasses = parseFilters(WFConfig.Instance().getNamespace(),".*\\.filters\\..*Filter");
    	List<FiterWrap> filters = new ArrayList<FiterWrap>();
		for(Iterator<Class<? extends WFFilter>> it = interceptorsClasses.iterator(); it.hasNext();){
			Class<? extends WFFilter> clazz = it.next();
			WFFilter interceptor = null;
			try {
				Filter annotation = clazz.getAnnotation(Filter.class);
				if(annotation!=null){
					interceptor = clazz.newInstance();
					FiterWrap iWrap = new FiterWrap(annotation.path(), annotation.order(), annotation.position(), interceptor);
					filters.add(iWrap);	
				}else{ //没有注解的按默认配置处理
					interceptor = clazz.newInstance();
					FiterWrap iWrap = new FiterWrap("", 0, Position.PRE_EXE, interceptor);
					filters.add(iWrap);
				}
				
			} catch (Exception e) {
				System.err.println("Build global filter failed, Interceptor: " + clazz.getName());
				throw new Exception("Build global filter failed!",e);
			}
			
		}
		// 根据order进行排序
        Collections.sort(filters, new FiterWrap.filterComparator());
        for (FiterWrap f : filters) {
        	System.out.println("Load filter : " + f.getInterceptor().getClass().getName());
        }
		globalFilters = ImmutableList.copyOf(filters);
	}

	@SuppressWarnings("unchecked")
	private static Set<Class<? extends WFFilter>> parseFilters(String packagePrefix, String filterPattern) {
		Set<Class<?>> classSet = ClassUtils.getClasses(packagePrefix);
	    Pattern fPattern = Pattern.compile(filterPattern);
	    ImmutableSet.Builder<Class<? extends WFFilter>> builder = ImmutableSet.builder();
	    for (Class<?> clazz : classSet)
	        if (rules(clazz, fPattern))
	            builder
	                .add((Class<? extends WFFilter>) clazz)
	                .build();

	    return builder.build();
	}
	
	private static boolean rules(Class<?> clazz, Pattern filterPattern) {

        return WFFilter.class.isAssignableFrom(clazz)
                && filterPattern.matcher(clazz.getName()).matches()
                && !Modifier.isInterface(clazz.getModifiers())
                && !Modifier.isAbstract(clazz.getModifiers())
                && Modifier.isPublic(clazz.getModifiers());
    }
        
}
