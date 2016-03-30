package org.spat.wf.mvc.exception;

import org.spat.wf.mvc.ActionResult;

public interface WFExceptionHandler<T extends Throwable> {
	
	public ActionResult handleException(T exception);

}

