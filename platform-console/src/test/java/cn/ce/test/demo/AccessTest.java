package cn.ce.test.demo;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import io.netty.handler.codec.http.HttpMethod;
import net.sf.json.JSONObject;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年2月6日
*/
public class AccessTest {
	
	private static String getewayApiUrl = "http://10.12.40.161:8000/openapi/siteConfigService/publishPageList.do";
	private static String rootTokenUrl = "http://10.12.40.161:8000/tyk/root/token";
	private static String urlSuffix = "/oauth/token?grant_type=client_credentials";
	private static String apiVersion = "v1";
	private static String appKey = "7abf20c3a8954ac690fc84a8bb8841b5";
	private static String secret = "eafd7ea93ae74f33b605a6bbca3baa5b";
	private static String rootKey = "root";
	private static String rootSecret = "openplatform@gateway";
	private static String saasId = "1600002957";
	private static String xTykAuthorization="352d20ee67be67f6340b4c0605b044b7";
	
	public static void main(String[] args) {

		//generalAccess();
		rootAccess();
	}
	
	
	//普通用户访问方式
	public static String generalAccess(){
		String token = null;
		String base64Str = Base64.getEncoder().encodeToString((appKey+":"+secret).getBytes());
		// 获取token
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Basic "+base64Str);//注意Basic后面的空格不能少
		String reStr = HttpService.putOrPostJson(getewayApiUrl+urlSuffix, null, headers, HttpMethod.POST);
		
		if(reStr != null){
			//解析token
			token = JSONObject.fromString(reStr).getString("access_token");
			
			//根据token调用接口，get或post与真实接口一致
			Map<String, String> accessHeaders = new HashMap<String, String>();
			accessHeaders.put("Authorization", "Bearer "+token);//注意Bearer后面的空格不能少
			accessHeaders.put("api-version", apiVersion);
			accessHeaders.put("App-Key", appKey);
			String resultStr = HttpService.putOrPostJson(getewayApiUrl, null, accessHeaders, HttpMethod.POST);
			
			System.out.println(resultStr);//返回结果
			return resultStr;
		}else{
			return null;
		}
	}
	
	//root用户访问方式
	public static String rootAccess(){
		String token = null;
		String base64Str = Base64.getEncoder().encodeToString((rootKey+":"+rootSecret).getBytes());
		// 获取token
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Basic "+base64Str);//注意Basic后面的空格不能少
		headers.put("X-Tyk-Authorization", xTykAuthorization);
		String reStr = HttpService.putOrPostJson(rootTokenUrl, null, headers, HttpMethod.POST);
		
		if(reStr != null){
			//解析token
			token = JSONObject.fromString(reStr).getString("access_token");
			
			//根据token调用接口，get或post与真实接口一致
			Map<String, String> accessHeaders = new HashMap<String, String>();
			accessHeaders.put("Authorization", "Bearer "+token);//注意Bearer后面的空格不能少
			accessHeaders.put("api-version", apiVersion);
			accessHeaders.put("Saas-Id", saasId);
			String resultStr = HttpService.putOrPostJson(getewayApiUrl, null, accessHeaders, HttpMethod.POST);
			
			System.out.println(resultStr);//返回结果
			return resultStr;
		}else{
			return null;
		}
	}
}
