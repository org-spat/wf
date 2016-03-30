package org.spat.wf.mvc.testers;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Before;
import org.spat.wf.mvc.WFBootstrap;

/**
 * 拦截器执行顺序测试
 */
public class InterceptorTest {
	
	private static WFBootstrap filter = new WFBootstrap();
	private static HttpServletRequest request = null;
	private static HttpServletResponse response = null;
	private static ServletContext context = null;
	private static RequestDispatcher dispatcher = null;
	private static FilterChain filterChain;
	
	private static final String uri = "/bothhome";
	
	@Before
	public void setup() {
		request = EasyMock.createMock(HttpServletRequest.class);
		response = EasyMock.createMock(HttpServletResponse.class);
		context = EasyMock.createMock(ServletContext.class);
		dispatcher = EasyMock.createMock(RequestDispatcher.class);
		filterChain = EasyMock.createMock(FilterChain.class);
		
		filterChain = EasyMock.createMock(FilterChain.class);
		EasyMock.replay(filterChain);
		
		FilterConfig filterConfig = EasyMock.createMock(FilterConfig.class);
		EasyMock.expect(filterConfig.getServletContext()).andReturn(context).times(2);
		
		EasyMock.expect(context.getRealPath("/")).andReturn("").times(6);
		EasyMock.replay(context);
		EasyMock.replay(filterConfig);
		
		try {
			filter.init(filterConfig);
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void testMain() throws IOException, ServletException{
		EasyMock.expect(request.getCharacterEncoding()).andReturn("UTF-8").times(1);
		EasyMock.expect(request.getMethod()).andReturn("POST").times(2);
		EasyMock.expect(request.getContentType()).andReturn("text/html").times(1);
		EasyMock.expect(request.getRequestURI()).andReturn(uri).times(1);
		EasyMock.expect(request.getContextPath()).andReturn("").times(1);
		EasyMock.expect(request.getRequestDispatcher("/resources/404.html")).andReturn(dispatcher).times(1);
		
//		PrintWriter writer = new PrintWriter(System.out);
//		EasyMock.expect(response.getWriter()).andReturn(writer);
		
		EasyMock.replay(request);
		EasyMock.replay(response);
		filter.doFilter(request, response, filterChain);
		
		EasyMock.verify(response);
	}

}

