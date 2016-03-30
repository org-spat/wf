package org.spat.wf.mvc.interceptor;

import java.util.regex.Pattern;

import org.spat.wf.mvc.BeatContext;
import org.spat.wf.mvc.annotation.Interceptor;
import org.spat.wf.mvc.initial.sys.InterceptorInit;

public final class InterceptorHandler {

	public static boolean excuteGlobalBeforeInterceptors() throws Exception {
		boolean result = true;
		BeatContext beat = BeatContext.current();
		for (InterceptorWrap interceptor : InterceptorInit.getGlobalInterceptors()) {
			if (interceptor.getPostition() == Interceptor.Position.PRE_EXE && matchPath(beat, interceptor.getPath())) {
				result = interceptor.getInterceptor().doFilter(beat);
				if (!result) {
					break; // 返回flase终止执行
				}
			}
		}
		return result;
	}

	public static Boolean excuteGlobalAfterInterceptors() throws Exception {
		boolean result = true;
		BeatContext beat = BeatContext.current();
		for (InterceptorWrap interceptor : InterceptorInit.getGlobalInterceptors()) {
			if (interceptor.getPostition() == Interceptor.Position.AFTER_EXE && matchPath(beat, interceptor.getPath())) {
				result = interceptor.getInterceptor().doFilter(beat);
				if (!result) {
					break;// 返回flase终止执行
				}
			}
		}
		return result;
	}

	private static Boolean matchPath(BeatContext beat, String path) {
		if (path == null || path.length() == 0) {
			return true;
		} else {
			return Pattern.compile(path,Pattern.CASE_INSENSITIVE).matcher(beat.getRequest().getContextPath()).matches();
		}
	}
}
