package org.spat.wf.mvc.exception;

import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.WFConfig;
import org.spat.wf.mvc.action.JsonActionResult;
import org.spat.wf.mvc.action.StringActionResult;

class DefaultWFExceptionHandler implements WFExceptionHandler<Throwable>{

	static final DefaultWFExceptionHandler INSTANCE = new DefaultWFExceptionHandler();
	
	private DefaultWFExceptionHandler() {}
	
	@Override
	public ActionResult handleException(Throwable exception) {
		exception.printStackTrace();
		String message = exception.getMessage();
		if(WFConfig.Instance().getMode()=="OFFLINE"){
			return new JsonActionResult(exception);
		}else{
			return new StringActionResult("WF exception : " + message);
		}
	}

}

