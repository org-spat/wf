package org.spat.wf.mvc.interceptors;

import org.spat.wf.mvc.BeatContext;
import org.spat.wf.mvc.WFInterceptor;

public class OutputInterceptor extends WFInterceptor{

	@Override
	public Boolean doFilter(BeatContext beat) throws Exception {
		System.out.println("excute cust OutputInterceptor..........................");
		return null;
	}

}

