package org.spat.wf.mvc.interceptors;

import org.spat.wf.mvc.BeatContext;
import org.spat.wf.mvc.WFFilter;

public class GlobalInterceptor extends WFFilter{

	@Override
	public Boolean doFilter(BeatContext beat) throws Exception {
		System.out.println("excute GlobalInterceptor before()..........................");
		return null;
	}

}

