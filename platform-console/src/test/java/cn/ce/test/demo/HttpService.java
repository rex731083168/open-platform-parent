package cn.ce.test.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http.HttpMethod;

public class HttpService {

	private static final Logger _LOGGER = LoggerFactory.getLogger(HttpService.class);

	public static String putOrPostJson(String url, String jsonBody, Map<String, String> headers, HttpMethod method) {

		_LOGGER.info("***********在代码中调用http/post or put /json请求***********");
		_LOGGER.info("url:" + url);
		_LOGGER.info("params:" + (null == jsonBody ? null : jsonBody.toString()));

		// 创建http client
		HttpClient httpClient = null;
		try {
			httpClient = getHttpClient();
		} catch (Exception e) {
			_LOGGER.info("create httpClient error");
			return null;
		}

		// 创建请求对象
		HttpEntityEnclosingRequestBase hrb;
		if (HttpMethod.POST == method) {
			hrb = new HttpPost(url);
		} else if (HttpMethod.PUT == method) {
			hrb = new HttpPut(url);
		} else {
			_LOGGER.info("该方法只支持post和put请求");
			return null;
		}

		// 添加头信息
		if (headers != null) {
			for (String key : headers.keySet()) {
				hrb.setHeader(key, headers.get(key));
			}
		}

		// 添加请求体参数
		if(StringUtils.isNotBlank(jsonBody)){
			StringEntity strEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
			strEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "utf-8"));
			hrb.setEntity(strEntity);
		}

		// 调用，并取回返回结果，打印返回状态码
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
		_LOGGER.info("response status:" + status.getStatusCode());
		if (status.getStatusCode() == 200) { // 只有状态为200才能够返回结果
			InputStream is = null;
			try {
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				String str = convertStreamToString(is);
				_LOGGER.info("返回body：" + str);
				is.close();
				return str;
			} catch (Exception e) {
				_LOGGER.error("200状态下拉取body数据失败");
				return null;
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
		} else { //返回状态非200
			try {
				String result = convertStreamToString(response.getEntity().getContent());
				_LOGGER.info("result:" + result);
			} catch (Exception e) {
				_LOGGER.info("error happens when read http result stream");
			}
			_LOGGER.error("返回状态不正确，请检查是否正确调用");
			return null;
		}
	}

	public static String putOrPostForm(String url, List<NameValuePair> params, Map<String, String> headers, HttpMethod method){

		_LOGGER.info("***********在代码中调用http/post or put /json请求***********");
		_LOGGER.info("url:" + url);
		_LOGGER.info("params:" + (null == params ? null : params));

		// 创建http client
		HttpClient httpClient = null;
		try {
			httpClient = getHttpClient();
		} catch (Exception e) {
			_LOGGER.info("create httpClient error");
			return null;
		}

		// 创建请求对象
		HttpEntityEnclosingRequestBase hrb;
		if (HttpMethod.POST == method) {
			hrb = new HttpPost(url);
		} else if (HttpMethod.PUT == method) {
			hrb = new HttpPut(url);
		} else {
			_LOGGER.info("该方法只支持post和put请求");
			return null;
		}

		// 添加头信息
		if (headers != null) {
			for (String key : headers.keySet()) {
				hrb.setHeader(key, headers.get(key));
			}
		}

		// 调用，并取回返回结果，打印返回状态码
		HttpResponse response = null;
		try {
			// 添加请求体参数
			if(null != params && params.size() >0){
				UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(params, "UTF-8");
				hrb.setEntity(reqEntity);
			}
			response = httpClient.execute(hrb);
		} catch (Exception e) {
			e.printStackTrace();
			_LOGGER.error("error happens when execute http post");
			return null;
		}

		StatusLine status = response.getStatusLine();

		_LOGGER.info("************resposne success************");
		_LOGGER.info("response status:" + status.getStatusCode());
		if (status.getStatusCode() == 200) { // 只有状态为200才能够返回结果
			InputStream is = null;
			try {
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				String str = convertStreamToString(is);
				_LOGGER.info("返回body：" + str);
				is.close();
				return str;
			} catch (Exception e) {
				_LOGGER.error("200状态下拉取body数据失败");
				return null;
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
		} else { //返回状态非200
			try {
				String result = convertStreamToString(response.getEntity().getContent());
				_LOGGER.info("result:" + result);
			} catch (Exception e) {
				_LOGGER.info("error happens when read http result stream");
			}
			_LOGGER.error("返回状态不正确，请检查是否正确调用");
			return null;
		}
	}

	public static String getOrDelParam(String url, Map<String, String> headers, HttpMethod method) {

		_LOGGER.info("***********在代码中调用http/ get or delete /json请求***********");
		_LOGGER.info("url:" + url);

		HttpClient httpClient = null;
		try {
			httpClient = getHttpClient();
		} catch (Exception e) {
			_LOGGER.info("get httpClient error");
			return null;
		}

		// 创建请求对象
		HttpRequestBase hrb;
		if (HttpMethod.DELETE == method) {
			hrb = new HttpDelete(url);
		} else if (HttpMethod.GET == method) {
			hrb = new HttpGet(url);
		} else {
			_LOGGER.info("只支持delete或get方法");
			return null;
		}

		// 添加头信息
		if (headers != null) {
			for (String key : headers.keySet()) {
				hrb.addHeader(key, headers.get(key));
			}
		}

		// 调用，并取回返回结果，打印返回状态码
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
		_LOGGER.info("response status:" + status.getStatusCode());
		if (200 == status.getStatusCode()) { // 只有状态为200才能够返回结果
			InputStream is = null;
			try {
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				String str = convertStreamToString(is);
				_LOGGER.info("返回body：" + str);
				is.close();
				return str;
			} catch (Exception e) {
				_LOGGER.error("200状态下拉取body数据失败");
				return null;
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			_LOGGER.error("返回状态不正确，请检查是否正确调用");
			return null;
		}
	}

	
	/**
	 * 
	 * @Description : 获取httpclient，解决跨域问题
	 * @Author : makangwei
	 * @Date : 2017年8月17日
	 */
	private static HttpClient getHttpClient()
			throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(),
				NoopHostnameVerifier.INSTANCE);
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", new PlainConnectionSocketFactory()).register("https", sslConnectionSocketFactory)
				.build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		cm.setMaxTotal(200);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory)
				.setConnectionManager(cm).build();
		return httpClient;
	}

	public static String convertStreamToString(InputStream is) {
		if (null == is) {
			return null;
		}
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			return null;
		}
		
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
