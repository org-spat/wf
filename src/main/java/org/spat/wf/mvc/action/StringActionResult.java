package org.spat.wf.mvc.action;

import java.io.IOException;
import java.io.PrintWriter;

import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.BeatContext;

public class StringActionResult extends ActionResult{
	
	private String str;
	
	public StringActionResult(String str) {
		this.str = str;
	}
	
	@Override
	public void render() {
		
		BeatContext beat = BeatContext.current();
		beat.getResponse().setHeader("Pragma", "No-cache");
		beat.getResponse().setHeader("Cache-Control", "no-cache");
		beat.getResponse().setHeader("Content-Type", "text/html; charset=utf-8");
		beat.getResponse().setCharacterEncoding("UTF-8");
		beat.getResponse().setDateHeader("Expires", -1);
		PrintWriter pw = null;
		
		try {
			
			pw = beat.getResponse().getWriter();
			pw.write(str);
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

