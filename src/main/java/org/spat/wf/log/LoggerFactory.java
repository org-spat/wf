package org.spat.wf.log;

import java.lang.reflect.Constructor;
import java.util.Map;

import org.spat.wf.mvc.WFConfig;

import com.google.common.collect.Maps;

public class LoggerFactory {

	static Map<String, ILogger> loggerMap = Maps.newHashMap();

	public static ILogger getLogger(Class<?> clazz) {
		ILogger logger = null;
		synchronized (loggerMap) {
			try {
				logger = loggerMap.get(clazz.getName());
				if (logger == null) {
					Constructor<?> con = Class.forName(WFConfig.Instance().getLogger().getType())
							.getConstructor(Class.class);
					logger = (ILogger) con.newInstance(clazz);
					loggerMap.put(clazz.getName(), logger);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return logger;
	}
	
	public static ILogger getLogger(String name) {
		ILogger logger = null;
		synchronized (loggerMap) {
			try {
				logger = loggerMap.get(name);
				if (logger == null) {
					Constructor<?> con = Class.forName(WFConfig.Instance().getLogger().getType())
							.getConstructor(String.class);
					logger = (ILogger) con.newInstance(name);
					loggerMap.put(name, logger);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return logger;
	}

}
