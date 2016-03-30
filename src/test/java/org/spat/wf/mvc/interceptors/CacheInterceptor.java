package org.spat.wf.mvc.interceptors;

import java.io.IOException;

import org.spat.wf.mvc.BeatContext;
import org.spat.wf.mvc.WFInterceptor;
import org.spat.wf.mvc.annotation.Interceptor;
import org.spat.wf.mvc.annotation.Interceptor.Position;

@Interceptor(path=".*",position=Position.PRE_EXE,order=1)
public class CacheInterceptor extends WFInterceptor{
	@Override
	public Boolean doFilter(BeatContext beat) throws IOException {
		System.out.println("excute cust CacheInterceptor..........................");
		beat.getResponse().getWriter().write("excute cust CacheInterceptor..........................");
		return true;
	}
}

