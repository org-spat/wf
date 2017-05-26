package org.spat.wf.mvc.initial.sys;

import java.util.HashMap;
import java.util.Map;

import org.spat.dao.converter.Converter;
import org.spat.wf.log.ILogger;
import org.spat.wf.log.LoggerFactory;
import org.spat.wf.mvc.WFConfig;
import org.spat.wf.mvc.dao.JsonConverter;

public class DaoInit {
	protected static ILogger logger = LoggerFactory.getLogger(DaoInit.class);
	public static void init() throws InstantiationException, IllegalAccessException{
		logger.info("Staring init dao...");
		Converter.register(new JsonConverter(), "JsonConverter");
		HashMap<String, Class<?>> converters = WFConfig.Instance().getDao().getConverters();
		for(Map.Entry<String, Class<?>> entry : converters.entrySet()){
			Converter converter = (Converter) entry.getValue().newInstance();
			if(converter!=null){
				Converter.register(converter, entry.getKey());
			}
		}
	}
}