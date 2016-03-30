package org.spat.wf.mvc.initial.sys;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.velocity.app.Velocity;
import org.spat.wf.mvc.WFConfig;

public class VelocityInit {

	private static Map<String, String> kvs = new HashMap<String, String>();
	
	static{
		kvs.put("resource.loader", "file");
		kvs.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		kvs.put("input.encoding", "UTF-8");
		kvs.put("output.encoding", "UTF-8");
		kvs.put("default.contentType", "text/html; charset=UTF-8");
		kvs.put("velocimarco.library.autoreload", "true");
		kvs.put("runtime.log.error.stacktrace", "false");
		kvs.put("runtime.log.warn.stacktrace", "false");
		kvs.put("runtime.log.info.stacktrace", "false");
		kvs.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
		kvs.put("runtime.log.logsystem.log4j.category", "velocity_log");		
		
		if ("ONLINE".equals(WFConfig.Instance().getMode())) {
			kvs.put("file.resource.loader.cache", "true");
			kvs.put("file.resource.loader.modificationCheckInterval", "0");
		} else {
			kvs.put("file.resource.loader.cache", "false");
			kvs.put("file.resource.loader.modificationCheckInterval", "2");
		}
	} 
	
	public static void init(ServletContext sc) throws Exception{
		Set<String> ks = kvs.keySet();
		for (String k : ks) {
			String value = kvs.get(k);
			Velocity.setProperty(k, value);
	
		}
		String webAppPath = sc.getRealPath("/");
       	Velocity.setProperty("file.resource.loader.path", webAppPath);
    	
    	try {
			Velocity.init();
		} catch (Exception e) {
        	System.err.println("Some Velocity properties maybe illegal, please recheck them.");
        	throw new Exception("Velocity init failed!",e);
		}
	}
	
	public static void setProperty(String key, String value) {
		kvs.put(key, value);
	}
}
