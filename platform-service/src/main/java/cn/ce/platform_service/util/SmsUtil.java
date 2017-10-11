package cn.ce.platform_service.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @类描述: 短信工具类
 * @作者 dingjia@300.cn
 * @日期 2016-7-4 下午5:28:40
 */
public class SmsUtil {

	/**
	 * 设置连接超期时间3s
	 */
	private final static int CONNECTION_TIMEOUT = 3000;
	/**
	 * 设置响应超期时间5s
	 */
	private final static int SO_TIMEOUT = 5000;
	private static final Logger log = LoggerFactory.getLogger(SmsUtil.class);

	/**
	 * 发送邮件
	 * @param synchFlag 是否同步发送
	 * 
	 */
	public static void send(final String phone, final String content, boolean synchFlag) {
		if (synchFlag) {// 同步发送
			messageSend(phone, content);
		} else {// 异步发送
			new Thread() {
				public void run() {
					messageSend(phone, content);
				};
			}.start();
		}

	}

	/**
	 * 
	 * @方法描述：发送短信
	 * @作者：dingjia@300.cn
	 * @日期：2016-7-4 下午5:28:48
	 * @param phone
	 * @param content
	 * void
	 */
	public static String messageSend(String phone, String content) {
		String postJson = "";
		Map<String,String> headers = new HashMap<>();
		String username = PropertiesUtil.getInstance().getValue("sms.username");
		String pwd = PropertiesUtil.getInstance().getValue("sms.pwd");
		String uri = PropertiesUtil.getInstance().getValue("sms.uri");
		try {
			headers.put("Authorization", "Basic " + Base64Utils.encode((username + ":" + pwd).getBytes()));
			
			JSONObject params = new JSONObject();
			params.put("content", content);
			params.put("phone", phone);
			params.put("providerId", "1");
			
			
			postJson = HttpUtils.postJsonNew(uri, params.toString(), headers);
			log.info(postJson);
			
			/*
			PostMethod postMethod = new PostMethod(uri);
			postMethod.addRequestHeader("Authorization", authorizationHeader);
			// 处理中文乱码
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			// 请求参数
			NameValuePair contentPair = new NameValuePair("content", content);
			NameValuePair phonePair = new NameValuePair("phone", phone);
			NameValuePair providerIdPair = new NameValuePair("providerId", "1");
			NameValuePair[] params = { contentPair, phonePair, providerIdPair };
			postMethod.setRequestBody(params);
			HttpClient client = new HttpClient();
			HttpConnectionManagerParams hcmp = client.getHttpConnectionManager().getParams();
			hcmp.setConnectionTimeout(CONNECTION_TIMEOUT);
			hcmp.setSoTimeout(SO_TIMEOUT);
			client.executeMethod(postMethod);
			String responseXml = new String(postMethod.getResponseBodyAsString().getBytes("UTF-8"));// 返回结果
			postMethod.releaseConnection();
			if (responseXml.contains("status")) {
				Document doc = null;
				doc = DocumentHelper.parseText(responseXml); // 将字符串转为XML
				Element rootElt = doc.getRootElement(); // 获取根节点
				String status = rootElt.element("status").getStringValue();
				if ("1".equals(status)) {
					return true;
				}
			}
			log.error(responseXml);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postJson;
	}
	
	/**
	 * 
	 * @方法描述：生成四位随机数
	 * @return
	 * String
	 */
	public static String getValidateCode() {
		String code = String.valueOf(Math.round(Math.random() * 10)) + String.valueOf(Math.round(Math.random() * 10))
		                + String.valueOf(Math.round(Math.random() * 10))
		                + String.valueOf(Math.round(Math.random() * 10));
		code = code.substring(0, 4);
		return code;
	}
	
}
