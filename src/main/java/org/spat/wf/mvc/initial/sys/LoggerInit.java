package org.spat.wf.mvc.initial.sys;

import java.io.File;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.spat.wf.mvc.WFConfig;

public class LoggerInit {

	public static void init() throws Exception {
		Log4jInit log4jInit = new Log4jInit();
		log4jInit.init();
	}

	public static class Log4jInit {

		public void init() throws Exception {
			Properties properties = getConfigLogProperties();
			if (properties == null){
				properties = defaultProperties();
			}
			PropertyConfigurator.configure(properties);
		}

		protected Properties getConfigLogProperties() throws Exception {
			if(WFConfig.Instance().getLogger().getProperties().size()>0){
				Properties properties = new Properties();
				for (Entry<String, String> entry : WFConfig.Instance().getLogger().getProperties().entrySet()) {  
					properties.put(entry.getKey(), entry.getValue());
				}
				return properties;
			}
			return null;
		}

		protected Properties defaultProperties() throws Exception {
			Properties properties = new Properties();
			properties.put("log4j.rootLogger", "INFO, file");
			properties.put("log4j.appender.file.File", defaultLogFile());
			properties.put("log4j.appender.file.DatePattern", "'.'yyyy-MM-dd");
			properties.put("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
			properties.put("log4j.appender.stdout.Target", "System.out");
			properties.put("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
			properties.put("log4j.appender.stdout.layout.ConversionPattern", "%m%n");
			properties.put("log4j.appender.file", "org.apache.log4j.DailyRollingFileAppender");
			properties.put("log4j.appender.file.Append", "true");
			properties.put("log4j.appender.file.Threshold", "INFO");
			properties.put("log4j.appender.file.layout", "org.apache.log4j.PatternLayout");
			properties.put("log4j.appender.file.layout.ConversionPattern", "%d{ABSOLUTE} %5p %c{1}:%L - %m%n");
			return properties;
		}

		private static String defaultLogFile() throws Exception {
			String projectId = WFConfig.Instance().getSiteName();
			if (projectId == null || projectId.length() == 0){
				projectId = "first";
			}
			File logFile = new File(WFConfig.Instance().getLogger().getProperties().get("logPath"), projectId + ".log");
			return logFile.getAbsolutePath();
		}
	}
}
