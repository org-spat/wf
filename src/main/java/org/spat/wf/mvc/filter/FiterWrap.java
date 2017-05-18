package org.spat.wf.mvc.filter;

import java.util.Comparator;

import org.spat.wf.mvc.WFFilter;
import org.spat.wf.mvc.annotation.Filter;

public class FiterWrap {
	private String path;
	private int order;
	private Filter.Position postition;
	private WFFilter filter;

	public FiterWrap(String path, int order, Filter.Position postition, WFFilter filter) {
		this.path = path;
		this.order = order;
		this.postition = postition;
		this.filter = filter;
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

	public Filter.Position getPostition() {
		return postition;
	}

	public void setPostition(Filter.Position postition) {
		this.postition = postition;
	}

	public WFFilter getInterceptor() {
		return filter;
	}

	public void setInterceptor(WFFilter filter) {
		this.filter = filter;
	}
	
	//public static InterceptorComparator comparator = new InterceptorComparator();

	public static class filterComparator implements Comparator<FiterWrap> {
		@Override
		public int compare(FiterWrap o1, FiterWrap o2) {
			return o1.order > o2.order ? 1 : 0;
		}

	}
}
