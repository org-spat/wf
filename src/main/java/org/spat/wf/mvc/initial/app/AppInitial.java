package org.spat.wf.mvc.initial.app;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.regex.Pattern;

import org.spat.wf.mvc.AppInit;
import org.spat.wf.mvc.WFConfig;
import org.spat.wf.utils.ClassUtils;

import com.google.common.collect.ImmutableSet;

public class AppInitial {

private static Set<Class<? extends AppInit>> inits ; 
	
	public static void initial() {
		
		inits = getInits();
		
		for(Class<? extends AppInit> init : inits) {
			
			Method initMethod;
			try {
				
				initMethod = init.getMethod("initial");
				initMethod.invoke(init.newInstance());
			} catch (Exception e) {
				
				e.printStackTrace();
			}

		}
	}
	
	private static Set<Class<? extends AppInit>> getInits(){
		
		Set<Class<? extends AppInit>> controllerClasses = parseInits(WFConfig.Instance().getNamespace() ,".*") ;
		
		return controllerClasses;
		
	}
	
	@SuppressWarnings("unchecked")
	private static Set<Class<? extends AppInit>> parseInits(String packagePrefix, String controllPattern) {

	    Set<Class<?>> classSet = ClassUtils.getClasses(packagePrefix);

	    Pattern initPattern = Pattern.compile(controllPattern);

	    ImmutableSet.Builder<Class<? extends AppInit>> builder = ImmutableSet.builder();

	    for (Class<?> clazz : classSet)
	        if (rules(clazz, initPattern)) 
	        	builder.add((Class<? extends AppInit>) clazz).build();

	    return builder.build();
	}
	
    private static boolean rules(Class<?> clazz, Pattern initPattern) {

        return AppInit.class.isAssignableFrom(clazz)
                && initPattern.matcher(clazz.getName()).matches()
                && !Modifier.isInterface(clazz.getModifiers())
                && !Modifier.isAbstract(clazz.getModifiers())
                && Modifier.isPublic(clazz.getModifiers());
    }
}
