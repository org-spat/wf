package org.spat.wf.mvc.action;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.BeatContext;
import org.spat.wf.mvc.Dispatcher.HttpMethod;
import org.spat.wf.mvc.WFInterceptor;

import com.google.common.collect.ImmutableSet;

/**
 * 对静态文件处理，把所有静态文件名保存在set中，如何精确匹配，表明当前请求就是静态文件
 * 
 */

public class ResourceAction extends Action {

	private String path;
	private Set<HttpMethod> supportMethods = HttpMethod.suportHttpMethods();

	public ResourceAction(String path) {

		this.path = path;
		initHttpMethods();
	}

	private void initHttpMethods() {
		supportMethods.add(HttpMethod.GET);
		supportMethods = ImmutableSet.copyOf(supportMethods);
	}

	@Override
	public String path() {
		return path;
	}

	@Override
	public ActionResult invoke() {
		return new ResourceActionResult();
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

	@Override
	public List<WFInterceptor> getInterceptor() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
