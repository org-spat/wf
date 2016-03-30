package org.spat.wf.mvc.toolbox.xss;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class XssConverter {
	
	private static Map<Character, String> encodeMatches = new HashMap<Character, String>();
	
	private static Map<String, Character> decodeMatches = new HashMap<String, Character>();
	

	public static String convert(String source){

		return source == null ? null : XssConverter.encode(source);
	}
	
	public static void initProperty(String configFile) throws Exception{
		
		File xssPropertyFile = new File(configFile);
		
		if(!xssPropertyFile.exists())
			return;

		Reader reader = null;
		try {

			reader = new FileReader(xssPropertyFile);
			Properties encodeProperties = new Properties();
			encodeProperties.load(reader);
			
			for (Enumeration<?> e = encodeProperties.propertyNames(); e.hasMoreElements();){
				
				String key = ((String)e.nextElement()).trim();
				char c = key.charAt(0);

				encodeMatches.put(c, encodeProperties.getProperty(key,""));
				decodeMatches.put(encodeProperties.getProperty(key,""), c);	
		    }			
			
		} catch (Exception e) {
			System.err.println("Xss property init failed, please recheck them.");
			throw new Exception("Xss property init failed", e);
		} finally{
			if(reader != null){
				try {
					reader.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}	
	}
	public static String encode(String str){
		
        if (str == null) {
            return null;
        }
        StringWriter writer = new StringWriter ((int)(str.length() * 1.5));
        
        for (int i = 0; i < str.length(); i++) {
        	char c = str.charAt(i);
        	if(encodeMatches.get(c) == null)
        		writer.write(c);
        	else
        		writer.write(encodeMatches.get(c));
        }
        return writer.toString();

	}
	
	public static String decodeHtml(String str) throws Exception{
	
		throw new Exception("no impl");	
	}
}
