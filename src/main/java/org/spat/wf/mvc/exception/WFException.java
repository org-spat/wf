package org.spat.wf.mvc.exception;

import java.util.Map;

import com.google.common.collect.Maps;

public class WFException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static volatile Map<Class<?>, WFExceptionHandler<?>> HANDLERS = Maps.newHashMap();
	
	public static synchronized <T extends Throwable> void setHandler(Class<T> clazz, WFExceptionHandler<T> handler) {
		HANDLERS.put(clazz, handler);
	}
	
	public static <T extends Throwable> WFExceptionHandler<?> getHandler(Class<T> clazz){
		WFExceptionHandler<?> handler = HANDLERS.get(clazz);
		
		if(handler==null)
			return DefaultWFExceptionHandler.INSTANCE;
		
		return handler;
	}
	
	public static WFExceptionHandler<Throwable> getDefaultExceptionHandler() {
		return DefaultWFExceptionHandler.INSTANCE;
	}
	
	public WFException(Throwable e) {
		super(e);
	}

}

