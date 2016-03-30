package org.spat.wf.mvc.cookies;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.spat.wf.mvc.BeatContext;

public class CookieManager {
	private Map<String, Cookie> MAP_COOKIES;
	private BeatContext beat;
	
	public CookieManager(BeatContext beat){
		this.beat = beat;
	}
	
	public void setCookie(Cookie cookie){
		 beat.getResponse().addCookie(cookie);
		 MAP_COOKIES.clear();
		 MAP_COOKIES = null;
	}
	
	public void setCookie(String name,String value,int maxAge){
		Cookie cookie = new Cookie(name,value);
	    cookie.setPath("/");
	    if(maxAge>0)  cookie.setMaxAge(maxAge);
	    beat.getResponse().addCookie(cookie);
	}
	
	public Cookie getCookie(String name){
		if(MAP_COOKIES==null){
			MAP_COOKIES = new HashMap<String, Cookie>();
			Cookie[] cookies =  beat.getRequest().getCookies();
	    	for(Cookie cookie : cookies){
	    		MAP_COOKIES.put(cookie.getName(), cookie);
	    	}
		}
		return MAP_COOKIES.get(name);
	}
}
