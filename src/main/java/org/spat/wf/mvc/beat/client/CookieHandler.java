package org.spat.wf.mvc.beat.client;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.spat.wf.mvc.BeatContext;

public class CookieHandler {

    private final HttpServletRequest request;
    
    private final HttpServletResponse response;
    
    private Cookie[] cookies = null;
    
    private static final Cookie[] emptyCookies = new Cookie[0];
	
	public CookieHandler(BeatContext beat) {
		
		this.request = beat.getRequest();
		this.response = beat.getResponse();
	}
	
    public void add(String name, String value) {
    	
        Cookie cookie = new Cookie(name, value);
        // 设置路径（默认）
        cookie.setPath("/");
        // 把cookie放入响应中
        add(cookie);
    }
    
    public void add(String name, String value, int cookieMaxAge) {
    	
        Cookie cookie = new Cookie(name, value);
        // 设置有效日期
        cookie.setMaxAge(cookieMaxAge);
        // 设置路径（默认）
        cookie.setPath("/");

        add(cookie);

    }
    
    public void add(Cookie cookie) {
       
    	response.addCookie(cookie);
    }
    
    public String get(String name) {
        
    	Cookie cookie = getCookie(name);
        return cookie == null?  null : cookie.getValue();
    }
    
    public Cookie getCookie(String name) {
        
    	Cookie[] cookies = getCookies();

        for(Cookie cookie : cookies){
            if(name.equalsIgnoreCase(cookie.getName()))
                return cookie;
        }

        return null;
    }
    
    public Cookie[] getCookies() {

        if (cookies != null)
            return cookies;

        cookies = request.getCookies();
        if (cookies == null)
            cookies = emptyCookies;

        return cookies;
    }
    
    public void remove(String name) {
        Cookie cookie = getCookie(name);

        if (cookie == null) return;

        // 销毁
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

	public void set(String name, String value) {
		Cookie cookie = getCookie(name);
		
		if (cookie == null){
			add(name, value);
			return;
		}
		
		cookie.setValue(value);
	}

	public void set(String name, String value, int time) {
		Cookie cookie = getCookie(name);
		
		if (cookie == null){
			add(name, value, time);
			return;
		}
		
		cookie.setValue(value);
		cookie.setMaxAge(time);
	}

	public void delete(String name) {
		 
		remove(name);
	}
    
}
