package org.spat.wf.mvc.beat.client;

import javax.servlet.http.HttpServletRequest;

import org.spat.wf.mvc.BeatContext;
import org.spat.wf.mvc.WFHttpServletRequestWrapper;

public class ClientInfo {
	
	private BeatContext beat;
	
	private CookieHandler cookie = null;	

	public ClientInfo(BeatContext beat) {
		
		this.beat = beat;
	}
	
	public CookieHandler getCookies() {
		
		if (cookie == null) {
			cookie = new CookieHandler(beat);
		}
				
		return cookie;	
	}
	
	public WFHttpServletRequestWrapper getUploads(){
		
		HttpServletRequest request = beat.getRequest();
		return  (request instanceof WFHttpServletRequestWrapper) 
			? (WFHttpServletRequestWrapper) beat.getRequest() 
			: null;
	}
	
	public boolean isUpload(){
		return getUploads() == null ? false : true;
	}
	
}
