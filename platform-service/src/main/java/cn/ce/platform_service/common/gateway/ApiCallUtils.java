package cn.ce.platform_service.common.gateway;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
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
import io.netty.handler.codec.http.HttpMethod;

/**
 *
 * @author makangwei
 * 2017-7-31
 */
public class ApiCallUtils {

	public static final Logger _LOGGER = LoggerFactory.getLogger(ApiCallUtils.class);

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
	 */
	public static boolean postGwJson(String url,JSONObject params) {
		
		_LOGGER.info("***********在java中调用http/post/json请求***********");
		_LOGGER.info("url:"+url);
		_LOGGER.info("params:"+params.toString());
		
		HttpClient httpClient = null;
		
		try{
			httpClient = getHttpClient();
		}catch(Exception  e){
			_LOGGER.info("create httpClient error");
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
			_LOGGER.error("error happens when execute http post");
			return false;
		}
		
		StatusLine status = response.getStatusLine();

		_LOGGER.info("************resposne success************");
		_LOGGER.info("response status:"+status.getStatusCode());
		if(status.getStatusCode() == 200){
			return true;	
		}
		
		return false;
	}
	
	public static String putOrPostMethod(String url, JSONObject params, Map<String,String> headers,HttpMethod method) {
		
		_LOGGER.info("***********在代码中调用http/post or put /json请求***********");
		_LOGGER.info("url:"+url);
		_LOGGER.info("params:"+params.toString());
		
		//创建http client
		HttpClient httpClient = null;
		try{
			httpClient = getHttpClient();
		}catch(Exception  e){
			_LOGGER.info("create httpClient error");
			return null;
		}
		
		//添加头信息
		HttpEntityEnclosingRequestBase hrb;
		if(HttpMethod.POST == method){
			hrb = new HttpPost(url);
		} else if(HttpMethod.PUT == method){
			hrb = new HttpPut(url);
		} else{
			_LOGGER.info("该方法只支持post和put请求");
			return null;
		}
		
		headers = (headers == null ? new HashMap<>() : headers);
		
		for (String key : headers.keySet()) {
			hrb.setHeader(key,headers.get(key));
		}
		
		//添加请求体参数
		StringEntity strEntity =  new StringEntity(params.toString(),ContentType.APPLICATION_JSON);
		strEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING,"utf-8"));
		hrb.setEntity(strEntity);
		
		//调用，并取回返回结果，打印返回状态码
		HttpResponse response = null;
		try {
			response = httpClient.execute(hrb);
			
		} catch (Exception e) {
			e.printStackTrace();
			_LOGGER.error("error happens when execute http post");
			return null;
		}
		
		StatusLine status = response.getStatusLine();

		_LOGGER.info("************resposne success************");
		_LOGGER.info("response status:"+status.getStatusCode());
		if(status.getStatusCode() == 200){ //只有状态为200才能够返回结果
			try{
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				String str = IOUtils.convertStreamToString(is);
				_LOGGER.info("返回body："+str);
				is.close();
				return str;
			}catch(Exception e){
				_LOGGER.error("200状态下拉取body数据失败");
				return null;
			}
		}else{
			_LOGGER.error("返回状态不正确，请检查是否正确调用");
			return null;
		}
	}
	
	public static String getOrDelMethod(String url, Map<String,String> headers, HttpMethod method){
		
		_LOGGER.info("***********在代码中调用http/ get or delete /json请求***********");
		_LOGGER.info("url:"+url);
		
		HttpClient httpClient = null;
		try{
			httpClient = getHttpClient();
		}catch(Exception e){
			_LOGGER.info("get httpClient error");
			return null;
		}

		//创建请求对象
		HttpRequestBase hrb;
		if(HttpMethod.DELETE == method){
			hrb = new HttpDelete(url);
		}else if(HttpMethod.GET == method){
			hrb = new HttpGet(url);
		}else{
			_LOGGER.info("只支持delete或get方法");
			return null;
		}
		
		//添加头信息
		for (String key : headers.keySet()) {
			hrb.addHeader(key,headers.get(key));
		}
		
		//调用，并取回返回结果，打印返回状态码
		HttpResponse response = null;
		try {
			response = httpClient.execute(hrb);
			
		} catch (Exception e) {
			e.printStackTrace();
			_LOGGER.error("error happens when execute http post");
			return null;
		}
		
		StatusLine status = response.getStatusLine();

		_LOGGER.info("************resposne success************");
		_LOGGER.info("response status:"+status.getStatusCode());
		if(status.getStatusCode() == 200){ //只有状态为200才能够返回结果
			try{
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				String str = IOUtils.convertStreamToString(is);
				_LOGGER.info("返回body："+str);
				is.close();
				return str;
			}catch(Exception e){
				_LOGGER.error("200状态下拉取body数据失败");
				return null;
			}
		}else{
			_LOGGER.error("返回状态不正确，请检查是否正确调用");
			return null;
		}
	}
	
	
	/**
	 * 向网关发送httpPut请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static boolean putGwJson(String url, JSONObject params) {
		
		_LOGGER.info("***********在java中调用http/put/json请求***********");
		_LOGGER.info("url:"+url);
		_LOGGER.info("params:"+params.toString());
		
		HttpClient httpClient = null;
		
		try{
			httpClient = getHttpClient();
		}catch(Exception e){
			_LOGGER.info("create httpClient error");
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
			_LOGGER.error("error happens when execute http put");
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
			_LOGGER.info("返回流:"+bt.toString());
			
			System.out.println(response.getEntity().getContent().toString());
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		_LOGGER.info("************resposne success************");
		_LOGGER.info("response status:"+status.getStatusCode());
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
			_LOGGER.info("create httpClient error");
			return false;
		}
		
		_LOGGER.info("reload path:"+basicUrl);
		HttpGet httpGet = new HttpGet(basicUrl);
		httpGet.addHeader(Constants.HEADER_KEY,Constants.HEADER_VALUE);
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (IOException e) {
			_LOGGER.info("error happens when execute http get");
			return false;
		}
		StatusLine statusLine = response.getStatusLine();
		
		_LOGGER.info("reload status:"+statusLine.getStatusCode());
		
		if(statusLine.getStatusCode() == 200){
			_LOGGER.info("reload success ");
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
			_LOGGER.info("get httpclient error");
			return false;
		}
		
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.addHeader(Constants.HEADER_KEY,Constants.HEADER_VALUE);
		
		HttpResponse response = null;
		
		try{
			response = httpClient.execute(httpGet);
		}catch(Exception e){
			_LOGGER.info("error happens when execute http get");
			return false;
		}
		StatusLine statusLine = response.getStatusLine();
		
		_LOGGER.info("get status:"+statusLine.getStatusCode());
		
		if(statusLine.getStatusCode() == 200){
			_LOGGER.info("get result success ");
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
			_LOGGER.info("get httpClient error");
			return false;
		}
		
		HttpDelete httpDelete = new HttpDelete(url);
		
		httpDelete.addHeader(Constants.HEADER_KEY,Constants.HEADER_VALUE);
		
		HttpResponse response = null;
		
		try{
			response = httpClient.execute(httpDelete);
		}catch(Exception e){
			_LOGGER.info("error happens when execute http delete");
			return false;
		}
		
		StatusLine statusLine = response.getStatusLine();
		
		_LOGGER.info("get delete status:"+statusLine.getStatusCode());
		
		if(statusLine.getStatusCode() == 200){
			_LOGGER.info("delete success");
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
			_LOGGER.info("get httpclient error");
			return null;
		}
		
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.addHeader(Constants.HEADER_KEY,Constants.HEADER_VALUE);
		
		HttpResponse response = null;
		
		try{
			response = httpClient.execute(httpGet);
		}catch(Exception e){
			_LOGGER.info("error happens when execute http get");
		}
		StatusLine statusLine = response.getStatusLine();
		
		_LOGGER.info("get status:"+statusLine.getStatusCode());
		
		if(statusLine.getStatusCode() == 200){
			
			_LOGGER.info("get api success ");
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			String str = IOUtils.convertStreamToString(is);
			return str;
		}
		return null;
	}
	
	


}
