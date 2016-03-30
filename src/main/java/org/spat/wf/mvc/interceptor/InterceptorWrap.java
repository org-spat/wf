package org.spat.wf.mvc.interceptor;

import java.util.Comparator;

import org.spat.wf.mvc.WFInterceptor;
import org.spat.wf.mvc.annotation.Interceptor;

public class InterceptorWrap {
	private String path;
	private int order;
	private Interceptor.Position postition;
	private WFInterceptor interceptor;

	public InterceptorWrap(String path, int order, Interceptor.Position postition, WFInterceptor interceptor) {
		this.path = path;
		this.order = order;
		this.postition = postition;
		this.interceptor = interceptor;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Interceptor.Position getPostition() {
		return postition;
	}

	public void setPostition(Interceptor.Position postition) {
		this.postition = postition;
	}

	public WFInterceptor getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(WFInterceptor interceptor) {
		this.interceptor = interceptor;
	}
	
	//public static InterceptorComparator comparator = new InterceptorComparator();

	public static class InterceptorComparator implements Comparator<InterceptorWrap> {
		@Override
		public int compare(InterceptorWrap o1, InterceptorWrap o2) {
			return o1.order > o2.order ? 1 : 0;
		}

	}
}
