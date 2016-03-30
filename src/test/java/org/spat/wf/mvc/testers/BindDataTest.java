package org.spat.wf.mvc.testers;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import org.spat.wf.mvc.WFBootstrap;

/**
 * 参数、beat，线程安全数据准确性测试
 */
public class BindDataTest {
	
	private static WFBootstrap filter = new WFBootstrap();
	private static HttpServletRequest request = null;
	private static HttpServletResponse response = null;
	private static ServletContext context = null;
	private static FilterChain filterChain;
	
	private static final String uri = "/home/zhangsan";
	
	@Before
	public void setup() {
		request = EasyMock.createMock(HttpServletRequest.class);
		response = EasyMock.createMock(HttpServletResponse.class);
		context = EasyMock.createMock(ServletContext.class);
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
	
	@Test
	public void testMain() throws IOException, ServletException{
		EasyMock.expect(request.getCharacterEncoding()).andReturn("UTF-8").times(1);
		EasyMock.expect(request.getMethod()).andReturn("GET").times(2);
		EasyMock.expect(request.getRequestURI()).andReturn(uri).times(2);
		EasyMock.expect(request.getContextPath()).andReturn("").times(1);
		EasyMock.expect(request.getParameter("name")).andReturn("lisi").times(1);
		EasyMock.expect(request.getParameter("age")).andReturn("18").times(1);
		EasyMock.expect(request.getParameter("company")).andReturn(null).times(1);
		
//		PrintWriter writer = new PrintWriter(System.out);
//		EasyMock.expect(response.getWriter()).andReturn(writer);
		
		EasyMock.replay(request);
		EasyMock.replay(response);
		filter.doFilter(request, response, filterChain);
		
		EasyMock.verify(response);
	}

}

