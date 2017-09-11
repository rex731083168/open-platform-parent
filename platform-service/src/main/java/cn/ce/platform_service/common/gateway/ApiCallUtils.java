package cn.ce.platform_service.common.gateway;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.IOUtils;

/**
 *
 * @author makangwei
 * 2017-7-31
 */
public class ApiCallUtils {

	public static final Logger LOGGER = LoggerFactory.getLogger(ApiCallUtils.class);

	/**
	 * 获取 HttpClient,解决跨域问
	 * @return HttpClient
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws KeyManagementException
	 */
	public static HttpClient getHttpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException{
		
		SSLContextBuilder builder = new SSLContextBuilder();
		
		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		
		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(),NoopHostnameVerifier.INSTANCE);
		
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http",new PlainConnectionSocketFactory())
				.register("https",sslConnectionSocketFactory)
				.build();
		
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		
		cm.setMaxTotal(200);
		
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(sslConnectionSocketFactory)
				.setConnectionManager(cm)
				.build();
		
		return httpClient;
	}
	
	/**
	 * 向网关发送httpPost请求
	 * @param url
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static boolean postGwJson(String url,JSONObject params) {
		
		LOGGER.info("***********在java中调用http/post/json请求***********");
		LOGGER.info("url:"+url);
		LOGGER.info("params:"+params.toString());
		
		HttpClient httpClient = null;
		
		try{
			httpClient = getHttpClient();
		}catch(Exception  e){
			LOGGER.info("create httpClient error");
			return false;
		}
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader(HTTP.CONTENT_TYPE,"application/json");
		httpPost.setHeader(Constants.HEADER_KEY,Constants.HEADER_VALUE);
		
		StringEntity strEntity =  new StringEntity(params.toString(),ContentType.APPLICATION_JSON);
		strEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING,"utf-8"));
		
		httpPost.setEntity(strEntity);
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("error happens when execute http post");
			return false;
		}
		
		StatusLine status = response.getStatusLine();

		LOGGER.info("************resposne success************");
		LOGGER.info("response status:"+status.getStatusCode());
		if(status.getStatusCode() == 200){
			return true;	
		}
		
		return false;
	}
	
	/**
	 * 向网关发送httpPut请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static boolean putGwJson(String url, JSONObject params) {
		
		LOGGER.info("***********在java中调用http/put/json请求***********");
		LOGGER.info("url:"+url);
		LOGGER.info("params:"+params.toString());
		
		HttpClient httpClient = null;
		
		try{
			httpClient = getHttpClient();
		}catch(Exception e){
			LOGGER.info("create httpClient error");
			return false;
		}
		
		HttpPut httpPut = new HttpPut(url);
		httpPut.setHeader(HTTP.CONTENT_TYPE,"application/json");
		httpPut.setHeader(Constants.HEADER_KEY,Constants.HEADER_VALUE);
		
		StringEntity strEntity =  new StringEntity(params.toString(),ContentType.APPLICATION_JSON);
		strEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING,"utf-8"));
		
		httpPut.setEntity(strEntity);
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPut);
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("error happens when execute http put");
			return false;
		}
		
		StatusLine status = response.getStatusLine();
		
		//解析返回实体
		try {
			StringBuffer sb = new StringBuffer();
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] bt = new byte[100];
			while(bis.read(bt) != -1){
				//String str = new String(bt);
				sb.append(bt);
			}
			LOGGER.info("返回流:"+bt.toString());
			
			System.out.println(response.getEntity().getContent().toString());
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LOGGER.info("************resposne success************");
		LOGGER.info("response status:"+status.getStatusCode());
		if(status.getStatusCode() == 200){
			return true;	
		}
		
		return false;
		
	}
	
	/**
	 * @Description : 单个网关节点重载get请求
	 * @Author : makangwei
	 * @Date : 2017年8月28日
	 */
	public static boolean getGwReload(String basicUrl){
		HttpClient httpClient = null;
		try{
			httpClient = getHttpClient();
		}catch(Exception e){
			LOGGER.info("create httpClient error");
			return false;
		}
		
		LOGGER.info("reload path:"+basicUrl);
		HttpGet httpGet = new HttpGet(basicUrl);
		httpGet.addHeader(Constants.HEADER_KEY,Constants.HEADER_VALUE);
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (IOException e) {
			LOGGER.info("error happens when execute http get");
			return false;
		}
		StatusLine statusLine = response.getStatusLine();
		
		LOGGER.info("reload status:"+statusLine.getStatusCode());
		
		if(statusLine.getStatusCode() == 200){
			LOGGER.info("reload success ");
			return true;
		}
		return false;
	}

	/**
	 * @Description : 向网关中发送get请求
	 * @Author : makangwei
	 * @Date : 2017年8月28日
	 * @param url 单个url不带参数
	 */
	public static boolean getGwJson(String url) {
		
		HttpClient httpClient = null;
		
		try{
			httpClient = getHttpClient();
		}catch(Exception e){
			LOGGER.info("get httpclient error");
			return false;
		}
		
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.addHeader(Constants.HEADER_KEY,Constants.HEADER_VALUE);
		
		HttpResponse response = null;
		
		try{
			response = httpClient.execute(httpGet);
		}catch(Exception e){
			LOGGER.info("error happens when execute http get");
			return false;
		}
		StatusLine statusLine = response.getStatusLine();
		
		LOGGER.info("get status:"+statusLine.getStatusCode());
		
		if(statusLine.getStatusCode() == 200){
			LOGGER.info("get result success ");
			return true;
		}
		
		return false;
	}

	/**
	 * @Description : 从网关中删除信息，delete方法
	 * @Author : makangwei
	 */
	public static boolean deleteGwJson(String url) {
		
		HttpClient httpClient = null;
		
		try{
			httpClient = getHttpClient();
		}catch(Exception e){
			LOGGER.info("get httpClient error");
			return false;
		}
		
		HttpDelete httpDelete = new HttpDelete(url);
		
		httpDelete.addHeader(Constants.HEADER_KEY,Constants.HEADER_VALUE);
		
		HttpResponse response = null;
		
		try{
			response = httpClient.execute(httpDelete);
		}catch(Exception e){
			LOGGER.info("error happens when execute http delete");
			return false;
		}
		
		StatusLine statusLine = response.getStatusLine();
		
		LOGGER.info("get delete status:"+statusLine.getStatusCode());
		
		if(statusLine.getStatusCode() == 200){
			LOGGER.info("delete success");
			return true;
		}
		return false;
	}

	/**
	 * @Description : 说明
	 * @Author : makangwei
	 * @Date : 2017年8月28日
	 */
	public static String getGwJsonApi(String url) throws UnsupportedOperationException, IOException {

		HttpClient httpClient = null;
		
		try{
			httpClient = getHttpClient();
		}catch(Exception e){
			LOGGER.info("get httpclient error");
			return null;
		}
		
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.addHeader(Constants.HEADER_KEY,Constants.HEADER_VALUE);
		
		HttpResponse response = null;
		
		try{
			response = httpClient.execute(httpGet);
		}catch(Exception e){
			LOGGER.info("error happens when execute http get");
		}
		StatusLine statusLine = response.getStatusLine();
		
		LOGGER.info("get status:"+statusLine.getStatusCode());
		
		if(statusLine.getStatusCode() == 200){
			
			LOGGER.info("get api success ");
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			String str = IOUtils.convertStreamToString(is);
			return str;
		}
		return null;
	}
	
	


}
