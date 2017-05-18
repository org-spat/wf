package org.spat.wf.mvc.action;

import java.util.List;

import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.WFInterceptor;

public abstract class Action {
   
    public abstract String path();
    
    public abstract List<WFInterceptor> getInterceptor();

    public abstract ActionResult invoke();
    
    public abstract boolean matchHttpMethod();
}
