package org.spat.wf.mvc;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.spat.wf.log.ILogger;
import org.spat.wf.log.LoggerFactory;
import org.spat.wf.mvc.exception.HTTP404Exception;
import org.spat.wf.mvc.exception.WFException;
import org.spat.wf.mvc.exception.WFExceptionHandler;
import org.spat.wf.mvc.initial.app.AppInitial;
import org.spat.wf.mvc.initial.sys.SysInitial;
import org.spat.wf.mvc.interceptor.InterceptorHandler;
import org.spat.wf.mvc.toolbox.monitor.ActionTimeMonitor;


/**
 * 利用Filter对请求进行拦截，并完成处理的整体流程。
 *
 */
@WebFilter(urlPatterns = {"/*"},
        dispatcherTypes = {DispatcherType.REQUEST},
        initParams = {@WebInitParam(name = "encoding", value = "UTF-8")},
        asyncSupported = true
)
public class WFBootstrap implements Filter{
	private Dispatcher dispatcher;
	private static AtomicBoolean hasInit = new AtomicBoolean(false);
	protected ILogger log = null;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if(hasInit.get()){
			return;
		}
		synchronized (WFBootstrap.class) {
			if(hasInit.get()){
				return;
			}
			hasInit.set(true);
			SysInitial.initial(filterConfig.getServletContext());
			log = LoggerFactory.getLogger(this.getClass());
			AppInitial.initial();
			dispatcher = new Dispatcher();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;
        if (request.getCharacterEncoding()==null){    	
	    	request.setCharacterEncoding(WFConfig.Instance().getCharset());
        }
        BeatContext beat = BeatContext.setup(httpReq, httpResp);
        ActionResult result = null;
        ActionTimeMonitor actionTimeMonitor = ActionTimeMonitor.Factory.create();
        try{
        	if(!InterceptorHandler.excuteGlobalBeforeInterceptors()){ //前置拦截器
        		return;
        	}
        	result = dispatcher.service(beat);
        	InterceptorHandler.excuteGlobalAfterInterceptors(); //后置拦截器
        	if(result!=null){
        		result.render();
        	}
        }catch(HTTP404Exception e){
        	chain.doFilter(request, response);
        }catch (Throwable e) {
			WFExceptionHandler handler = WFException.getHandler(e.getClass());
			result = handler.handleException(e);
			result.render();
		} finally {
			actionTimeMonitor.post();
        	BeatContext.clear();
        }
	}

	@Override
	public void destroy() {
		
	}	
}
