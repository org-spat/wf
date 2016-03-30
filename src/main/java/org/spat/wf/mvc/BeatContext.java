/*
*  Copyright Beijing 58 Information Technology Co.,Ltd.
*
*  Licensed to the Apache Software Foundation (ASF) under one
*  or more contributor license agreements.  See the NOTICE file
*  distributed with this work for additional information
*  regarding copyright ownership.  The ASF licenses this file
*  to you under the Apache License, Version 2.0 (the
*  "License"); you may not use this file except in compliance
*  with the License.  You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package org.spat.wf.mvc;


import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.spat.wf.mvc.action.Action;
import org.spat.wf.mvc.beat.client.ClientInfo;
import org.spat.wf.mvc.beat.server.ServerInfo;
import org.spat.wf.mvc.cookies.CookieManager;

import com.google.common.collect.Maps;


/**
 * 管理一个客户端请求的生命周期
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class BeatContext implements Cloneable{
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private Model model;
	
    private ClientInfo clientInfo;
    
    private ServerInfo serverInfo;
    
    private CookieManager cookieManager;
    
    private Action action;
    
	private static final ThreadLocal<BeatContext> STORE = new ThreadLocal<BeatContext>();
	
	private BeatContext(){}
    
	private BeatContext(HttpServletRequest request, HttpServletResponse response) {
		
		this.request = request;
		this.response = response;
        this.clientInfo = new ClientInfo(this);
        this.serverInfo = new ServerInfo(this);
        this.model = new Model();
        this.cookieManager = new CookieManager(this);
        
	}    
		
	
	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	static BeatContext setup(HttpServletRequest request, HttpServletResponse response){
		request = WFHttpServletRequestWrapper.wrapper(request);
		BeatContext beat = new BeatContext(request, response);
		STORE.set(beat);
		return beat;
	}
	
	static void clear(){
		STORE.remove();
	}
	
	public static BeatContext current(){
		return STORE.get();
	}
		
	public HttpServletRequest getRequest() {
		
		return request;
	}

	public HttpServletResponse getResponse() {
		
		return response;
	}

	public Model getModel() {
		
		return model;
	}
	
	public ClientInfo getClient() {
		
		return clientInfo;
	}
	
	public ServerInfo getServer() {
		
		return serverInfo;
	}
	
	public Object getSession(String name){
		return request.getSession().getAttribute(name);
	}
	public void setSession(String name,Object value){
		request.getSession().setAttribute(name, value);
	}
	
	public String getCookie(String name){
		Cookie cookie = cookieManager.getCookie(name);
    	if(cookie!=null){
    		String value = cookie.getValue();
    		if(value==null){
    			value = "";
    		}
    		return value;
    	}
    	return "";
	}
	
	public void setCookie(String name,String value,int maxAge){
		cookieManager.setCookie(name,value,maxAge);
	}
	
	
	@Override
	public Object clone() {
		BeatContext currentBeat = current();
		BeatContext copyBeat = new BeatContext();
		copyBeat.request = currentBeat.request;
		copyBeat.response = null;
		copyBeat.clientInfo = currentBeat.clientInfo;
		copyBeat.serverInfo = currentBeat.serverInfo;
		copyBeat.model = currentBeat.model;
		return copyBeat;
	}
	
	public class Model {
		
		private Map<String, Object> modelMap;
		
		private Model() {
			
		}
		
		private Map<String, Object> getModelMap() {
			if (this.modelMap == null) {
				this.modelMap = Maps.newHashMap();
			}
			return this.modelMap;
		}
		
	    /**
	     * 增加一个属性
	     * @param attributeName 属性名称
	     * @param attributeValue 属性值
	     */
	    public Model add(String attributeName, Object attributeValue) {
	    	getModelMap().put(attributeName, attributeValue);
	        return this;
	    }
	    
	    /**
	     * 根据属性名得到属性值
	     * @param attributeName 属性名称
	     * @return 对应的属性值
	     */
	    public Object get(String attributeName) {
	        return getModelMap().get(attributeName);
	    }
	    
	    /**
	     * Return the model map. Never returns <code>null</code>.
	     * To be called by application code for modifying the model.
	     */
	    public Map<String, Object> getModel() {
	        return getModelMap();
	    }
	    
	    /**
	     * 批量增加属性
	     * @param attributes
	     */
	    public Model addAll(Map<String, ?> attributes) {
	    	getModelMap().putAll(attributes);
	        return this;
	    }
	    
	    /**
	     * 判断是否包含属性名
	     * @param attributeName 需要查找的属性
	     * @return
	     */
	    public boolean contains(String attributeName) {
	        return getModelMap().containsKey(attributeName);
	    }
	  
		
		/**
		 * 合并属性
		 * @param attributes
		 */		
		public Model merge(Map<String, ?> attributes) {
			if (attributes != null) {
				for (String key : attributes.keySet()) {
					if (!getModelMap().containsKey(key)) {
						getModelMap().put(key, attributes.get(key));
					}
				}
			}
			return this;
		}
	}
}