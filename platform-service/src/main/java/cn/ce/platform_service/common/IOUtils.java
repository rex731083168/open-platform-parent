package cn.ce.platform_service.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Description : 说明
 * @Author : makangwei
 * @Date : 2017-8-8
 */
public class IOUtils {

	 public static String convertStreamToString(InputStream is) {      
		  
		 BufferedReader reader = new BufferedReader(new InputStreamReader(is));      
	   
		 StringBuilder sb = new StringBuilder();      
	
		 String line = null;      
	
		 try {      
	
			 while ((line = reader.readLine()) != null) {      
	        
				 sb.append(line);      
			 }      
	     
		 } catch (IOException e) {      
			 e.printStackTrace();      
		 } finally {   
			 try {      
				 is.close();      
			 } catch (IOException e) {      
				 e.printStackTrace();      
	         }      
	     }      
		 return sb.toString();      
	 } 
}
