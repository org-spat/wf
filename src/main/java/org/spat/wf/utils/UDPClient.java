package org.spat.wf.utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import org.spat.wf.log.ILogger;
import org.spat.wf.log.LoggerFactory;

public class UDPClient {

	private DatagramSocket sock;
	
	private InetSocketAddress addr;
	
	private ILogger timeoutLog = LoggerFactory.getLogger(this.getClass());
	
	public UDPClient(String ip, int port) {
		try {
			
			sock = new DatagramSocket();
			addr = new InetSocketAddress(ip, port);
			
		} catch (Exception e) {
			
			timeoutLog.error("UDPClient init error.", e);
		}
	}
	
	public void close() {
		sock.close();
	}
	
	public void send(String msg, String encode) {
		
		try {
			
			byte[] buf = msg.getBytes(encode);
			send(buf);
		} catch (Exception e) {
			timeoutLog.error("UDPClient send error.", e);
		}
		
	}
	
	public void send(String msg) {
		
		try {
			byte[] buf = msg.getBytes("utf-8");
			send(buf);
		} catch (Exception e) {
			timeoutLog.error("UDPClient send error.", e);
		}
		
	}
	
	public void send(byte[] buf) {
		
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length, addr);
			sock.send(dp);
		} catch (Exception e) {
			timeoutLog.error("UDPClient send error.", e);
		}
		
	}
}