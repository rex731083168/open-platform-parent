//package cn.ce.platform_service.oauth.service;
//
//import java.util.Map;
//
//import cn.ce.platform_service.common.Result;
//
///**
//* @Description : 说明
//* @Author : makangwei
//* @Date : 2017年8月17日
//*/
//public interface IOauthService {
//
//	Object getClientIdAndSecret(String apiId, String redirectUri);
//
//	/***
//	 * 根据参数访问tyk网关生成code
//	 * @param request 
//	 * @param clientId
//	 * @param response_type
//	 * @param redirect_uri
//	 * @throws Exception
//	 */
//	Result<String> generalAuthCode(String requestUri, String clientId,String response_type,String redirect_uri) throws Exception;
//	int findByFields(Map<String, Object> queryMap);
//
//}
