package org.spat.wf.mvc.interceptors;

import org.spat.wf.mvc.BeatContext;
import org.spat.wf.mvc.WFInterceptor;

public class ResponseDataInterceptor extends WFInterceptor{
	@Override
	public Boolean doFilter(BeatContext beat) throws Exception {
		System.out.println("excute ResponseDataInterceptor..........................");
		return null;
	}

}

