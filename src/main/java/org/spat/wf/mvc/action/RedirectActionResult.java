package org.spat.wf.mvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.BeatContext;

public class RedirectActionResult extends ActionResult{
	private String path;
	public RedirectActionResult(String path){
		this.path = path;
	}
	
	@Override
	public void render() throws IOException {

		BeatContext beat = BeatContext.current();
		HttpServletResponse response = beat.getResponse();
		response.sendRedirect(path);
	}
}
