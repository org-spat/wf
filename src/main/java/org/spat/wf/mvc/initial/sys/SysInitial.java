package org.spat.wf.mvc.initial.sys;

import javax.servlet.ServletContext;

import org.spat.wf.mvc.WFConfig;

public class SysInitial {

	public static void initial(ServletContext sc) {
		try {
			WFConfig.Init(sc.getRealPath("/"));
			LoggerInit.init();
			ActionInit.init(sc);
			InterceptorInit.init();
			VelocityInit.init(sc);
			XssInit.init();
			DaoInit.init();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("SysInitial failed!!!");
			System.exit(0);
		}

	}
}
