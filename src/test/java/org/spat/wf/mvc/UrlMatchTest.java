//package org.spat.wf.mvc;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.easymock.EasyMock;
//import org.junit.Test;
//
//import org.spat.wf.mvc.annotation.Path;
//
//public class UrlMatchTest {
//	
//	public static class CustomController extends WFController{
//		@Path("/a/b/c/{name}")
//		public ActionResult show(final String name){
//			return new ActionResult() {
//				@Override
//				public void render(BeatContext beatContext) {
//					System.out.println("==========="+name);
//				}
//			};
//		}
//		
//		@Path(value = { "/a/b/c/{name}/{age}" })
//		public ActionResult show2(final String name, final int age, final User user){
//			return new ActionResult() {
//				@Override
//				public void render(BeatContext beatContext) {
//					System.out.println("==========="+name+","+age+","+user);
//				}
//			};
//		}
//	}
//	
//	private static HttpServletRequest request;
//	private static HttpServletResponse response;
//	private static String uri = "/a/b/c/zhangsan/18";
//	
//	static{
//		ControllerInfo controllerInfo = new ControllerInfo(new CustomController());
//		controllerInfo.analyze();
//		
//		request = EasyMock.createMock(HttpServletRequest.class);
//		EasyMock.expect(request.getRequestURI()).andReturn(uri).times(2);
//		EasyMock.expect(request.getContextPath()).andReturn("/").times(1);
//		EasyMock.expect(request.getParameter("name")).andReturn("lisi").times(1);
//		EasyMock.expect(request.getParameter("age")).andReturn("18").times(1);
//		EasyMock.expect(request.getParameter("company")).andReturn(null).times(1);
//		EasyMock.replay(request);
//		
//		response = EasyMock.createMock(HttpServletResponse.class);
//		EasyMock.replay(response);
//	}
//	
//	
//	
//	@Test
//	public void test1() throws Exception{
//		BeatContext beat = BeatContext.setup(request, response);
//		
//		// 通过uri取到映射的ActionInfo
//		ActionInfo matchActionInfo = UrlMappingResolver.matchUrl(uri);
//		
//		// 执行action方法，获得返回值ActionResult
//		ActionResult result = matchActionInfo.invoke();
//		
//		// 响应
//		result.render(beat);
//	}
//
//}
//
