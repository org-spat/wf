package org.spat.wf.mvc.initial.sys;

import org.spat.wf.mvc.WFConfig;
import org.spat.wf.mvc.toolbox.xss.XssConverter;

public class XssInit {

	public static void init() throws Exception {
		String xssPropertyPath = WFConfig.Instance().getConfigPath()+ "/xss.properties";
		XssConverter.initProperty(xssPropertyPath);
	}
}
