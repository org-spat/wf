package org.spat.wf.mvc.session;

import javax.servlet.http.HttpSession;

import org.spat.wf.mvc.WFConfig;
import org.spat.wf.mvc.WFConfig.Session;

public class SessionProvider {
	
	public static HttpSession getSession() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		Session session = WFConfig.Instance().getSession();
		if(session!=null && !session.getType().isEmpty() && !session.getType().toLowerCase().equals("default")){
			return (HttpSession) Class.forName(session.getType()).newInstance();
		}
		return null;
	}
}
