package org.spat.wf.mvc.action;

import java.io.IOException;
import java.io.PrintWriter;

import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.BeatContext;
import org.spat.wf.mvc.WFConfig;
import org.spat.wf.utils.JsonUtil;

public class JsonActionResult extends ActionResult{
	
	private Object obj;
	
	public JsonActionResult(Object obj) {
		this.obj = obj;
	}
	
	@Override
	public void render() {
		
		BeatContext beat = BeatContext.current();
		beat.getResponse().setHeader("Pragma", "No-cache");
		beat.getResponse().setHeader("Cache-Control", "no-cache");
		beat.getResponse().setDateHeader("Expires", -1);
		beat.getResponse().setHeader("Content-Type", "application/json; charset=utf-8");
		beat.getResponse().setCharacterEncoding(WFConfig.Instance().getCharset());
		
		PrintWriter pw = null;
		try {
			pw = beat.getResponse().getWriter();
			pw.write(JsonUtil.toJson(obj));
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

