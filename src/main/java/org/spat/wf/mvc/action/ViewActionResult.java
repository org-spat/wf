package org.spat.wf.mvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.io.VelocityWriter;

import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.BeatContext;
import org.spat.wf.mvc.WFConfig;

public class ViewActionResult extends ActionResult{

    private final String suffix = ".html";

    private final String viewName;

    public ViewActionResult(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render() throws IOException {
    	BeatContext beat = BeatContext.current();
        String path = "views" + "\\" + viewName + suffix;
        Template template =  Velocity.getTemplate(path);
        HttpServletResponse response = beat.getResponse();       
        response.setContentType("text/html;charset=\""+WFConfig.Instance().getCharset()+"\"");
        response.setCharacterEncoding(WFConfig.Instance().getCharset());
        Context context = new VelocityContext(beat.getModel().getModel());
        VelocityWriter vw = null;
        try {
            vw = new VelocityWriter(response.getWriter());
            template.merge(context, vw);
            vw.flush();
        } finally {
            vw.recycle(null);
        }
    }

	public static ActionResult view(String viewName) {

		return new ViewActionResult(viewName);
	}
}

