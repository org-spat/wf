package org.spat.wf.mvc.interceptor;

import java.util.List;

import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.BeatContext;
import org.spat.wf.mvc.WFInterceptor;
import org.spat.wf.mvc.action.Action;

public final class InterceptorHandler {

	public static ActionResult excutePreInterceptor(Action action) throws Exception {
		ActionResult result = null;
		BeatContext beat = BeatContext.current();
		List<WFInterceptor> interceptors = action.getInterceptor();
		for (WFInterceptor interceptor : interceptors) {
			result = interceptor.before(beat);
			if(result!=null){
				break;
			}
		}
		return result;
	}

	public static void excutePostInterceptor(Action action,ActionResult actionResult) throws Exception {
		BeatContext beat = BeatContext.current();
		List<WFInterceptor> interceptors = action.getInterceptor();
		for (WFInterceptor interceptor : interceptors) {
			interceptor.after(beat, actionResult);
		}
	}
}
