package org.spat.wf.mvc.action;

import java.io.IOException;
import java.io.PrintWriter;

import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.BeatContext;
import org.spat.wf.mvc.WFConfig;

public class ExceptionActionResult extends ActionResult{
	
	private Throwable execepiton;
	
	public ExceptionActionResult(Throwable execepiton) {
		this.execepiton = execepiton;
	}
	
	@Override
	public void render() {
		
		BeatContext beat = BeatContext.current();
		beat.getResponse().setHeader("Pragma", "No-cache");
		beat.getResponse().setHeader("Cache-Control", "no-cache");
		beat.getResponse().setDateHeader("Expires", -1);
		beat.getResponse().setHeader("Content-Type", "text/plain; charset=utf-8");
		beat.getResponse().setCharacterEncoding(WFConfig.Instance().getCharset());
		
		PrintWriter pw = null;
		try {
			pw = beat.getResponse().getWriter();
			execepiton.printStackTrace(pw);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(pw!=null){
				pw.close();
			}
		}
	}

}

