package org.spat.wf.mvc;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.spat.wf.mvc.action.Action;
import org.spat.wf.mvc.action.AntPathMatcher;
import org.spat.wf.mvc.action.MethodAction;
import org.spat.wf.mvc.exception.HTTP404Exception;
import org.spat.wf.mvc.initial.sys.ActionInit;
import org.spat.wf.utils.PathUtils;

import com.google.common.collect.Maps;

public class Dispatcher {
	
	private AntPathMatcher pathMatcher = new AntPathMatcher();

	public ActionResult service(BeatContext beat) throws HTTP404Exception {
		Action action = findAction(beat);
		if(action == null){
			//return new HttpStatusActionResult(404);
			throw new HTTP404Exception();
		}else{
			return action.invoke();
		}		
	}

	private Action findAction(BeatContext beat) {

		String uri = beat.getRequest().getRequestURI();
		String contextPath = beat.getRequest().getContextPath();
		String relativeUrl = uri.substring(contextPath.length());
		String simplyPath = PathUtils.simplyWithoutSuffix(relativeUrl);
		String bagPath = PathUtils.simplyWithoutSuffix(simplyPath);
		Map<String, String> uriTemplateVariables = Maps.newHashMap();

		Map<String, List<MethodAction>> ministyActions = ActionInit
				.getMinistyActions();
		List<Action> exactActionMap = ActionInit.getExactActions(bagPath);

		Action ea = null;
		if (exactActionMap != null) {
			for (Action action : exactActionMap) {
				if (action.matchHttpMethod()) {
					ea = action;
					break;
				}
			}
		}

		if (ea != null) {
			return ea;
		}

		if (ministyActions != null) {
			for (String path : ministyActions.keySet()) {
				boolean match = pathMatcher.doMatch(path, bagPath, true,
						uriTemplateVariables);
				if (!match) {
					continue;
				}
				List<MethodAction> actions = ministyActions.get(path);
				for (MethodAction action : actions) {
					if (action.matchHttpMethod()) {
						return action;
					}
				}
			}
		}

		return null;
	}

	public enum HttpMethod {

		GET,

		POST,

		HEAD;

		public static String parse(String method) {
			if (method == null || method.length() == 0)
				return null;

			return method.toUpperCase();
		}

		public static Set<HttpMethod> suportHttpMethods() {
			Set<HttpMethod> methods = new HashSet<HttpMethod>();
			methods.add(HEAD);
			return methods;
		}

	}

}

