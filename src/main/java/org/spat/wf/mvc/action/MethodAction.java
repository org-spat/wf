package org.spat.wf.mvc.action;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.BeatContext;
import org.spat.wf.mvc.Dispatcher.HttpMethod;
import org.spat.wf.mvc.WFController;
import org.spat.wf.mvc.beat.bind.RequestBinder;
import org.spat.wf.mvc.exception.WFException;
import org.spat.wf.utils.PathUtils;
import org.spat.wf.utils.PrimitiveConverter;

import com.google.common.collect.ImmutableSet;

/**
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class MethodAction extends Action {

	protected WFController controller;

	protected Method method;

	protected String pathPattern;

	/**
	 * 方法上所有参数名，按顺序排列
	 */
	protected List<String> paramNames;

	/**
	 * 方法上所有参数类型，按顺序排列
	 */
	protected List<Class<?>> paramTypes;

	/**
	 * 所有annotation，包括并覆盖controller上的annotation，
	 */
	protected Set<Annotation> annotations;

	/**
	 * 是否是通配符匹配
	 */
	protected boolean isPattern;

	//private List<WFInterceptor> interceptors = Lists.newArrayList();

	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	private final PrimitiveConverter converter = new PrimitiveConverter();

	private Set<HttpMethod> supportMethods = HttpMethod.suportHttpMethods();

	public MethodAction(WFController controller, Method method,
			String pathPattern, boolean isGet, boolean isPost,
			List<String> paramNames, List<Class<?>> paramTypes,
			Set<Annotation> annotations) throws Exception {

		this.controller = controller;
		this.method = method;
		this.pathPattern = pathPattern;
		this.paramNames = paramNames;
		this.paramTypes = paramTypes;
		this.annotations = annotations;
		this.isPattern = pathMatcher.isPattern(pathPattern)|| paramTypes.size() > 0;
		initHttpMethods(isGet, isPost);
	}

	private void initHttpMethods(boolean isGet, boolean isPost) {
		if (isGet) {
			supportMethods.add(HttpMethod.GET);
		}
		if (isPost) {
			supportMethods.add(HttpMethod.POST);
		}
		supportMethods = ImmutableSet.copyOf(supportMethods);
	}


	public Method getMethod() {
		return method;
	}


	@Override
	public String path() {

		return pathPattern;
	}

	public boolean isPattern() {
		return isPattern;
	}

	private Object[] matchValues() {
		if (paramTypes.isEmpty()) {
			return null;
		}

		Object[] params = new Object[paramTypes.size()];

		BeatContext beat = BeatContext.current();
		String uri = beat.getRequest().getRequestURI();
		
		String contextPath = beat.getRequest().getContextPath();		
		String relativeUrl = uri.substring(contextPath.length());
		String simplyPath = PathUtils.simplyWithoutSuffix(relativeUrl);
		uri= PathUtils.simplyWithoutSuffix(simplyPath);
		
		
		Map<String, String> urlParams = pathMatcher
				.extractUriTemplateVariables(this.pathPattern, uri);
		for (int i = 0; i < paramNames.size(); i++) {
			String paramName = paramNames.get(i);
			Class<?> clazz = paramTypes.get(i);

			String v = urlParams.get(paramName);
			// 普通类型直接bind
			if (v != null) {
				if (converter.canConvert(clazz)) {
					params[i] = converter.convert(clazz, v);
					continue;
				}
			}

			if (converter.canConvert(clazz))
				continue;

			params[i] = RequestBinder.bindAndValidate(clazz);
		}
		return params;
	}

	@Override
	public ActionResult invoke() {
		Object result = null;
		try {
			result = method.invoke(controller, matchValues());
		} catch (IllegalArgumentException e) {
			throw new WFException(e);
		} catch (IllegalAccessException e) {
			throw new WFException(e);
		} catch (InvocationTargetException e) {
			throw new WFException(e);
		}
		return (ActionResult) result;
	}

	@Override
	public boolean matchHttpMethod() {
		String requestMethod = BeatContext.current().getRequest().getMethod();
		String currentMethod = HttpMethod.parse(requestMethod);
		HttpMethod httpMethod = null;
		try {
			httpMethod = HttpMethod.valueOf(currentMethod);
		} catch (Exception e) {
			return false;
		}

		return supportMethods.contains(httpMethod);
	}

}
