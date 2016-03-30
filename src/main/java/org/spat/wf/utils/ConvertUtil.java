package org.spat.wf.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertUtil {
	public static int toInt(Object obj){
		if(obj==null){
			return 0;
		}
		return Integer.valueOf(obj.toString());
	}
	public static long toLong(Object obj){
		if(obj==null){
			return 0L;
		}
		return Long.valueOf(obj.toString());
	}
	public static boolean toBoolean(Object obj){
		if(obj==null){
			return false;
		}
		return Boolean.valueOf(obj.toString());
	}
	public static String toString(Object obj){
		if(obj==null){
			return "";
		}
		return obj.toString();
	}
	
	public static <K,V> Map<K,V> List2Map(List<V> lists,IL2M<K,V> converter){
		Map<K,V> result = new HashMap<K,V>();
		for(V obj : lists){
			converter.confert(result, obj);
		}
		return result;
	}
	
	public interface IL2M<K,V>{
		void confert(Map<K,V> result,V obj);
	}
}
