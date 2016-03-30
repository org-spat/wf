package org.spat.wf.mvc.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.BeatContext;
import org.spat.wf.utils.PathUtils;

/**
 *
 * 处理静态文件
 */
public class ResourceActionResult extends ActionResult{

	@Override
	public void render() {
		
		BeatContext beat = BeatContext.current();
		HttpServletRequest request = beat.getRequest();
		HttpServletResponse response = beat.getResponse();

		try {
 
			String uri = beat.getRequest().getRequestURI();
			String contextPath = beat.getRequest().getContextPath();
			String relativeUrl = uri.substring(contextPath.length());
			String simplyPath = PathUtils.simplyWithoutSuffix(relativeUrl);
			String bagPath = PathUtils.simplyWithoutSuffix(simplyPath);	
			
			request.getRequestDispatcher("/resources" + bagPath).forward(request, response);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
