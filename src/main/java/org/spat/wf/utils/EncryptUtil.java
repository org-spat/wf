package org.spat.wf.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {
	
	public final static String MD5(String s) throws NoSuchAlgorithmException {
		 char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		 byte[] btInput = s.getBytes();
	     MessageDigest mdInst = MessageDigest.getInstance("MD5");// 获得MD5摘要算法的 MessageDigest 对象
         mdInst.update(btInput);// 使用指定的字节更新摘要
         byte[] md = mdInst.digest();// 获得密文
         int j = md.length;// 把密文转换成十六进制的字符串形式
         char str[] = new char[j * 2];
         int k = 0;
         for (int i = 0; i < j; i++) {
             byte byte0 = md[i];
             str[k++] = hexDigits[byte0 >>> 4 & 0xf];
             str[k++] = hexDigits[byte0 & 0xf];
         }
         return new String(str);
    }
	
	public final static String Variant4MD5(String s) throws NoSuchAlgorithmException {
		String salt = "xiaolin!@#";
		String s2 = MD5(s);
		StringBuffer sb = new StringBuffer(s2);
		return MD5(sb.reverse().append(salt).substring(salt.length()));
	}
}