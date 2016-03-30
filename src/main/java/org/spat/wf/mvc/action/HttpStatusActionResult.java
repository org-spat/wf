package org.spat.wf.mvc.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.BeatContext;

public class HttpStatusActionResult extends ActionResult{

	private int status;
	
	public HttpStatusActionResult(int status){
		this.status = status;
	}
	
	@Override
	public void render() {

		BeatContext beat = BeatContext.current();
		HttpServletRequest request = beat.getRequest();
		HttpServletResponse response = beat.getResponse();
		try {
			request.getRequestDispatcher("/resources/" + status + ".html").forward(request, response);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
