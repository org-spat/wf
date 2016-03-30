package org.spat.wf.mvc;

import java.io.IOException;

/**
 * 所有Action的返回结果
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class ActionResult {


    /**
     * 用于生成显示页面
     *
     * @param beatContext 需要渲染的上下文
     */
    public abstract void render() throws IOException;
   
    protected BeatContext beat() {
		
		return BeatContext.current();
	}
    
    public void setParams(String name,Object value){
    	beat().getModel().add(name, value);
    }
   
}
