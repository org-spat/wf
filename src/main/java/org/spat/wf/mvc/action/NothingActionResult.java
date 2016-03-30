package org.spat.wf.mvc.action;

import org.spat.wf.mvc.ActionResult;

public class NothingActionResult extends ActionResult{
	private static NothingActionResult nar = new NothingActionResult();
	@Override
	public void render() {
	}
	
	public static ActionResult Nothing(){
		return nar;
	}
}

