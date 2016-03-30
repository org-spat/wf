package org.spat.wf.mvc.toolbox.trace;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.spat.wf.log.ILogger;
import org.spat.wf.log.LoggerFactory;
import org.spat.wf.mvc.BeatContext;

public class Trace {

	
	private static final String customTraceTitle = "<table width='100%'><tr colspan='2'  bgcolor='#333366'>CUSTOM TRACE INFO : </tr><tr><td>CONTENT</td><td>WASTETIME</td></tr>";
	private static final String sysTraceTitle = "<table width='100%'><tr colspan='2'  bgcolor='#333366'>SYS TRACE INFO : </tr><tr><td>TITLE</td><td>CONTENT</td></tr>";
	private static final String tableEnd = "</table>";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private final static String traceInfoDesc = "__TRACEINFO";
	
	protected static final ILogger logger = LoggerFactory.getLogger(Trace.class);
	

	public static void trace(String content){
		
		BeatContext beat = BeatContext.current();		
		TraceInfo traceInfo = (TraceInfo) beat.getModel().get(traceInfoDesc);
		
		if(traceInfo == null)
			return;
		
		CustomTraceInfo cti = new CustomTraceInfo();
		cti.setContent(content);
		long curTime = System.currentTimeMillis();
		cti.setWasteTime(curTime - traceInfo.getLastTraceTime());
		traceInfo.setLastTraceTime(curTime);
		traceInfo.getCustomTraceInfos().add(cti);
	}
	/**
	 * 兼容
	 * @param content
	 * @param beat
	 */
	public static void trace(String content, BeatContext beat){
		trace(content);
		
	}
	public static void init(){
		BeatContext beat = BeatContext.current();		
		String trace = beat.getRequest().getParameter("trace"+sdf.format(new Date()));
		
		if(trace == null || !trace.equals("true"))
			return;
		
		TraceInfo traceInfo = new TraceInfo();
    	traceInfo.setArriveTime(System.currentTimeMillis());
    	traceInfo.setLastTraceTime(System.currentTimeMillis());
    	traceInfo.setCustomTraceInfos(new ArrayList<CustomTraceInfo>());
    	traceInfo.setReqInfo(new RequestTraceInfo());
    	beat.getModel().add(traceInfoDesc, traceInfo);    	
	}
	public static void wrapper(){
		BeatContext beat = BeatContext.current();	

		if( (TraceInfo) beat.getModel().get(traceInfoDesc) == null)
			return ;
		
		StringBuilder sb = new StringBuilder();
		
		getCustomInfo(sb);		
		
		getSysInfo(sb);
		
		try {
			
			beat.getResponse().getWriter().write(sb.toString());
		} catch (Exception e) {
			
			logger.error("error when wrapper Trace Response",e);
		}
	}
	
	private static void getCustomInfo(StringBuilder sb){
		
		BeatContext beat = BeatContext.current();
		sb.append(customTraceTitle);
		
		TraceInfo traceInfo = (TraceInfo) beat.getModel().get(traceInfoDesc);
		
		for(CustomTraceInfo cti : traceInfo.getCustomTraceInfos()){
			
			sb.append("<tr>");
			sb.append("<td>" + cti.getContent() + "</td>");
			sb.append("<td>" + cti.getWasteTime() + "</td>");
			sb.append("</tr>");
			
		}
		sb.append(tableEnd);
		
	}
	
	private static void getSysInfo(StringBuilder sb){
		sb.append(sysTraceTitle);
		sb.append(tableEnd);
	}
}
