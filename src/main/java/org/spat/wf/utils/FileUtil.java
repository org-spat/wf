package org.spat.wf.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {
	public static String ReadAllText(String path,String encoding) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path),encoding)); 
		StringBuffer sb = new StringBuffer();
		String data;  
		while( true){  
		      data = br.readLine(); //接着读下一行  
		      if(data==null){
		    	  break;
		      }
		      sb.append(data);
		      
		}
		br.close();
		return sb.toString();
	}
	
	public static boolean isExist(String path){
		File f = new File(path);
		return f.exists();
	}
}
