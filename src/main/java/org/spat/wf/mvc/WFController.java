package org.spat.wf.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.spat.wf.log.ILogger;
import org.spat.wf.log.LoggerFactory;
import org.spat.wf.mvc.cookies.CookieManager;

public abstract class WFController{

	protected ILogger _LOG = LoggerFactory.getLogger(this.getClass());
	protected CookieManager cookieManager;
	
    protected BeatContext beat() {
		return BeatContext.current();
	}
    protected HttpServletRequest request() {
		return BeatContext.current().getRequest();
	}
    protected HttpServletResponse response() {
		return BeatContext.current().getResponse();
	}
    
    protected HttpSession session() {
		return BeatContext.current().getRequest().getSession();
	}
    protected String getCookie(String name) {
    	return beat().getCookie(name);
	}
    protected void setCookie(String name,String value,int maxAge) {
    	beat().setCookie(name, value, maxAge);
	}
    
    //---------------------------------------------------------------------------//
    
    protected int getIntPramater(String name,int... defult) {
    	int value = 0;
    	String p = request().getParameter(name);
		if(p!=null && !p.isEmpty()){
			value = Integer.parseInt(p);
		}else {
			if(defult!=null && defult.length>0){
				value = defult[0];
			}
		}
		return value;
	}
    
    protected long getLongPramater(String name,long... defult) {
    	long value = 0;
    	String p = request().getParameter(name);
		if(p!=null && !p.isEmpty()){
			value = Long.parseLong(p);
		}
		return value;
	}
    
    protected float getFloatPramater(String name,long... defult) {
    	float value = 0;
    	String p = request().getParameter(name);
		if(p!=null && !p.isEmpty()){
			value = Float.parseFloat(p);
		}
		return value;
	}
    
    protected Boolean getBooleanPramater(String name) {
    	Boolean value = false;
    	String p = request().getParameter(name);
		if(p!=null && !p.isEmpty()){
			value = Boolean.parseBoolean(p);
		}
		return value;
	}
    
    protected String getStrPramater(String name) {
    	String value = "";
    	String p = request().getParameter(name);
		if(p!=null){
			value = p;
		}
		return value;
	}
}

