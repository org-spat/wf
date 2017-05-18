package org.spat.wf.mvc.filter;

import java.util.regex.Pattern;

import org.spat.wf.mvc.BeatContext;
import org.spat.wf.mvc.annotation.Filter;
import org.spat.wf.mvc.initial.sys.FilterInit;

public final class FilterHandler {

	public static boolean excuteGlobalBeforeFilters() throws Exception {
		boolean result = true;
		BeatContext beat = BeatContext.current();
		for (FiterWrap interceptor : FilterInit.getGlobalFilters()) {
			if (interceptor.getPostition() == Filter.Position.PRE_EXE && matchPath(beat, interceptor.getPath())) {
				result = interceptor.getInterceptor().doFilter(beat);
				if (!result) {
					break; // 返回flase终止执行
				}
			}
		}
		return result;
	}

	public static Boolean excuteGlobalAfterFilters() throws Exception {
		boolean result = true;
		BeatContext beat = BeatContext.current();
		for (FiterWrap interceptor : FilterInit.getGlobalFilters()) {
			if (interceptor.getPostition() == Filter.Position.AFTER_EXE && matchPath(beat, interceptor.getPath())) {
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
