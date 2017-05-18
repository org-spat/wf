package org.spat.wf.mvc.exception;

import org.spat.wf.log.ILogger;
import org.spat.wf.log.LoggerFactory;
import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.WFConfig;
import org.spat.wf.mvc.action.ExceptionActionResult;
import org.spat.wf.mvc.action.StringActionResult;

class DefaultWFExceptionHandler implements WFExceptionHandler<Throwable>{

	protected static final DefaultWFExceptionHandler INSTANCE = new DefaultWFExceptionHandler();
	private static ILogger logger = LoggerFactory.getLogger(WFExceptionHandler.class);
	
	private DefaultWFExceptionHandler() {}
	
	@Override
	public ActionResult handleException(Throwable exception) {
		if(WFConfig.Instance().getMode().equals("OFFLINE")){
			exception.printStackTrace();
			return new ExceptionActionResult(exception);
		}else{
			long erroId = System.currentTimeMillis();
			logger.error("错误ID："+erroId, exception);
			return new StringActionResult("系统发生错误，错误ID : " + erroId);
			
		}
	}

}

