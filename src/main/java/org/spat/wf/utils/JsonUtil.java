package org.spat.wf.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * @author wangzt
 * @version 1.1.0
 * @since 2011-12-7
 */
public class JsonUtil {

    private static Gson gson  = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
   
    public static String toJson(Object obj) {
    	
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }
    
    public static void setDateFormat(String formatStr) {
    	gson =  new GsonBuilder().setDateFormat(formatStr).create();
	}
}
