package org.spat.wf.mvc.toolbox.trace;

import java.util.List;


/**
 * @author liuzw 
 *
 */
public class TraceInfo {
	
	private long arriveTime;
	
	private long lastTraceTime;
	
	private long leaveTime;
	
	private RequestTraceInfo reqInfo;
	
//	private ResponseTraceInfo resInfo;
	
	private List<CustomTraceInfo> customTraceInfos;
	
	public RequestTraceInfo getReqInfo() {
		return reqInfo;
	}

	public void setReqInfo(RequestTraceInfo reqInfo) {
		this.reqInfo = reqInfo;
	}

//	public ResponseTraceInfo getResInfo() {
//		return resInfo;
//	}
//
//	public void setResInfo(ResponseTraceInfo resInfo) {
//		this.resInfo = resInfo;
//	}

	public long getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(long arriveTime) {
		this.arriveTime = arriveTime;
	}

	public long getLastTraceTime() {
		return lastTraceTime;
	}

	public void setLastTraceTime(long lastTraceTime) {
		this.lastTraceTime = lastTraceTime;
	}
	
	public long getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(long leaveTime) {
		this.leaveTime = leaveTime;
	}

	public List<CustomTraceInfo> getCustomTraceInfos() {
		return customTraceInfos;
	}

	public void setCustomTraceInfos(List<CustomTraceInfo> customTraceInfos) {
		this.customTraceInfos = customTraceInfos;
	}
}
