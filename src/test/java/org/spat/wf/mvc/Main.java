package org.spat.wf.mvc;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;

import org.spat.wf.mvc.WFBootstrap;

public class Main {
	
	private static String uri = "/bothhome";
	private static WFBootstrap filter = new WFBootstrap();
	private static FilterChain filterChain;
	
	static{
		HttpServletRequest request;
		HttpServletResponse response;
		FilterConfig filterConfig;
		ServletContext servletContext;
		
		servletContext = EasyMock.createMock(ServletContext.class);
		EasyMock.expect(servletContext.getRealPath("/")).andReturn("").times(5);
		EasyMock.replay(servletContext);
		
		request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.expect(request.getRequestURI()).andReturn(uri).times(2);
		EasyMock.expect(request.getContextPath()).andReturn("/").times(2);
		EasyMock.expect(request.getParameter("name")).andReturn("lisi").times(1);
		EasyMock.expect(request.getParameter("age")).andReturn("18").times(1);
		EasyMock.expect(request.getParameter("company")).andReturn(null).times(1);
		EasyMock.replay(request);
		
		response = EasyMock.createMock(HttpServletResponse.class);
		EasyMock.replay(response);
		
		filterConfig = EasyMock.createMock(FilterConfig.class);
		EasyMock.expect(filterConfig.getServletContext()).andReturn(servletContext).times(2);
		EasyMock.replay(filterConfig);
		
		filterChain = EasyMock.createMock(FilterChain.class);
		EasyMock.replay(filterChain);
		
		
		try {
			filter.init(filterConfig);
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws ServletException, IOException {
		new Thread(){
			public void run() {
				try {
					TimeUnit.HOURS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		RequestDispatcher requestDispatcher;
		
		request = EasyMock.createMock(HttpServletRequest.class);
		response = EasyMock.createMock(HttpServletResponse.class);
		
		IMocksControl requestDispatcherControl = EasyMock.createControl();
		requestDispatcher = requestDispatcherControl.createMock(RequestDispatcher.class);
//		EasyMock.expect(requestDispatcher.forward(request, response)).andReturn("UTF-8").times(1);
		
		EasyMock.replay(requestDispatcher);
		
		
		EasyMock.expect(request.getCharacterEncoding()).andReturn("UTF-8").times(1);
		EasyMock.expect(request.getMethod()).andReturn("POST").times(2);
		EasyMock.expect(request.getContentType()).andReturn("text/html").times(1);
		EasyMock.expect(request.getRequestURI()).andReturn(uri).times(1);
		EasyMock.expect(request.getContextPath()).andReturn("/").times(1);
		EasyMock.expect(request.getRequestDispatcher("/resources/404.html")).andReturn(requestDispatcher).times(1);
		
		requestDispatcher.forward(request, response);
		EasyMock.expectLastCall();
		EasyMock.replay(request);
		EasyMock.replay(response);
		
		try {
			filter.doFilter(request, response, filterChain);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		
	}

}

