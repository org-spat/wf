package org.spat.wf.mvc.beat.bind;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.spat.wf.log.ILogger;
import org.spat.wf.log.LoggerFactory;
import org.spat.wf.mvc.BeatContext;
import org.spat.wf.utils.PrimitiveConverter;

/**
 * WF自实现的错误对象实体
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class RequestBinder {
	
	public static Object bindAndValidate(Class<?> clazz){
		
		Object target = instantiateClass(clazz);
		
		BeatContext beat = BeatContext.current();
		
		Object BindResult = bind(target, beat);
		
		return BindResult;	
		
	}
	
	public static Object bind(Object target, BeatContext beat){		
		
		ILogger logger = LoggerFactory.getLogger(RequestBinder.class);
		
		PrimitiveConverter converter = new PrimitiveConverter();
		
		Field[] fields = target.getClass().getDeclaredFields();
		
		for(Field field : fields){
			
			Class<?> clazzClass = field.getType();
			HttpServletRequest request = beat.getRequest();
			String reqParaValue = request.getParameter(field.getName());
			if(converter.canConvert(clazzClass) && reqParaValue!=null && !"".equals(reqParaValue)){
				try {
					
					Object object = converter.convert(clazzClass, reqParaValue);
					Method m=(Method) target.getClass().getMethod("set" + getMethodName(field.getName()),field.getType());
					m.invoke(target, object);
				} catch (Exception e) {				
					logger.error("Param Bind Error: [targetName=" + target.getClass().getName()  + ", fieldName=" + field.getName() + "]", e);
				}
			}			
		}	

		return target;
	}
	
    private static String getMethodName(String fildeName) throws Exception{  
        byte[] items = fildeName.getBytes();  
        items[0] = (byte) ((char) items[0] - 'a' + 'A');  
        return new String(items);  
    } 
    
	public static <T> T instantiateClass(Class<T> clazz) {
	
		if (clazz.isInterface()) {
			System.out.println("Specified class is an interface");
		}
		try {
			return instantiateClass(clazz.getDeclaredConstructor());
		}
		catch (NoSuchMethodException ex) {
			System.out.println("No default constructor found");
		}
		return null;
	}
	
	public static <T> T instantiateClass(Constructor<T> ctor, Object... args)  {

	try {

		return ctor.newInstance(args);
	} catch (Exception ex) {
		System.out.println("instantiateClass error");
	}
	return null;
}
}
