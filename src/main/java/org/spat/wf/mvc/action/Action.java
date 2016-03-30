package org.spat.wf.mvc.action;

import org.spat.wf.mvc.ActionResult;

public abstract class Action {
   
    public abstract String path();
    
    public abstract ActionResult invoke();
    
    public abstract boolean matchHttpMethod();
}
