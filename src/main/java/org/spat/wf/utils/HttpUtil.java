package org.spat.wf.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.ws.http.HTTPException;

public class HttpUtil {
	public static String doGet(String url) throws IOException{
		Map<String, String> header = new HashMap<String, String>();
		header.put("User-Agent", "Application-WF vesion-3.0");
		return doGet(url,header);
	}
	
	public static String doGet(String url,Map<String, String> header) throws IOException{
		return doGet(url,header,"GB2312");
	}
	
	public static String doGet(String url,Map<String, String> header,String charset) throws IOException{
		URL uri = new  URL(url);
		HttpURLConnection  httpURLConn= (HttpURLConnection)uri.openConnection();
		httpURLConn.setConnectTimeout(15000);  
        httpURLConn.setDoOutput(true);
        httpURLConn.setRequestMethod("GET");

        Iterator<Entry<String, String>> iter = header.entrySet().iterator();
        Entry<String, String> entry;
        String key,value;
        while (iter.hasNext()) {
            entry = iter.next();
            key = entry.getKey();
            value = entry.getValue();
            httpURLConn.setRequestProperty(key, value);
        }
        
        httpURLConn.connect();
        InputStream in =httpURLConn.getInputStream();
        BufferedReader bd = new BufferedReader(new InputStreamReader(in,Charset.forName(charset)));
        //System.out.println("+++++++++++++++++++---++");
        String temp;
        StringBuffer sb = new StringBuffer();
        while((temp=bd.readLine())!=null)
        {
        	sb.append(temp);
        }
        bd.close();
        httpURLConn.disconnect();
        String txt = new String(sb.toString().getBytes(), charset); 
        return txt;
        //return sb.toString();
	}
	
	public static String doPost(String url,String data) throws IOException{
		Map<String, String> header = new HashMap<String, String>();
		header.put("User-Agent", "test");
		return doPost(url,data,header);
	}
	
	public static String doPost(String url,String data,Map<String, String> header) throws IOException{
		URL uri = new  URL(url);
		HttpURLConnection  httpURLConn= (HttpURLConnection)uri.openConnection();
		httpURLConn.setConnectTimeout(15000); 
		httpURLConn.setDoOutput(true);  
		httpURLConn.setDoInput(true); 
        httpURLConn.setRequestMethod("POST");
        
        Iterator<Entry<String, String>> iter = header.entrySet().iterator();
        Entry<String, String> entry;
        String key,value;
        while (iter.hasNext()) {
            entry = iter.next();
            key = entry.getKey();
            value = entry.getValue();
            httpURLConn.setRequestProperty(key, value);
        }
        
        PrintWriter printWriter = new PrintWriter(httpURLConn.getOutputStream());  
        printWriter.write(data);  
        printWriter.flush();  
        int responseCode = httpURLConn.getResponseCode();  
        if (responseCode != 200) {  
           throw new HTTPException(responseCode);
        }
        InputStream in =httpURLConn.getInputStream();
        BufferedReader bd = new BufferedReader(new InputStreamReader(in,Charset.forName("UTF-8")));
        String temp;
        StringBuffer sb = new StringBuffer();
        while((temp=bd.readLine())!=null)
        {
        	sb.append(temp);
        }
        bd.close();
        httpURLConn.disconnect();
        return sb.toString();
	}
}
