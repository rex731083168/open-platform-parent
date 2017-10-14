package cn.ce.platform_manage.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.json.JSONObject;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月10日
*/
public class JsonFileReadTest {

	public static void main(String[] args) throws IOException {
		
		System.out.println(System.getProperty("user.dir"));
		
		File file = new File(System.getProperty("user.dir")+"/src/main/java/cn/ce/platform_manage/test/apiToGateway.json");
		
		Reader is = new FileReader(file);
		
		BufferedReader bis = new BufferedReader(is);
		
		StringBuffer sb = new StringBuffer();
		
		String temp = null;
		while((temp = bis.readLine()) != null){
			sb.append(temp);
		}
		
		
		System.out.println(sb);
		
		JSONObject job = new JSONObject(sb.toString());
		
		
	}
}
