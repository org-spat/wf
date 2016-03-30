package org.spat.wf.mvc.toolbox.trace;

public class RequestTraceInfo {

	private String sessionId; 
	
	private String reqType;
	
	private String reqTime;
	
	private String reqEncoding;
	
	private String status;
	
	private String resEncoding;
	
	private String header;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getReqTime() {
		return reqTime;
	}

	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}

	public String getReqEncoding() {
		return reqEncoding;
	}

	public void setReqEncoding(String reqEncoding) {
		this.reqEncoding = reqEncoding;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResEncoding() {
		return resEncoding;
	}

	public void setResEncoding(String resEncoding) {
		this.resEncoding = resEncoding;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
}
