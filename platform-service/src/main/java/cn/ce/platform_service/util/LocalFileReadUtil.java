package cn.ce.platform_service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
* @Description : 读取本读文件工具类
* @Author : makangwei
* @Date : 2017年10月13日
*/
public class LocalFileReadUtil {

	private static Logger _LOGGER = LoggerFactory.getLogger(LocalFileReadUtil.class);
	
	public static JSONObject readLocalClassPathJson(String localUrl){
		
		_LOGGER.info("读取本地json文件，dir:"+localUrl);
		
		File file = null;
		try {
			file = new ClassPathResource(localUrl).getFile();
		} catch (IOException e1) {
			_LOGGER.info("file read error when reading local json file");
			return null;
		}
		
		if(file == null || !file.exists()){
			_LOGGER.info("file does not exits when reading local json file");
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		JSONObject job = null;
		try{
			Reader is = new FileReader(file);
			@SuppressWarnings("resource")
			BufferedReader bis = new BufferedReader(is);
			String temp = null;
			while((temp = bis.readLine()) != null){
				sb.append(temp);
			}
			_LOGGER.info("本地json文件内容"+sb);
			job = new JSONObject(sb.toString());
		}catch(Exception e){
			_LOGGER.error("error happens when execute local json read",e);
			return null;
		}
		
		return job;
	}
	
}
