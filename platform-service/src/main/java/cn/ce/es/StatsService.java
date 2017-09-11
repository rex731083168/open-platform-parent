package cn.ce.es;

import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;

/**
 *
 * @Title: StatsService.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年9月1日 time下午1:45:06
 *
 **/
public interface StatsService {
	/**
	 * 
	 * @param param
	 * @param dateTime
	 * @param apiKey
	 * @param type
	 * @return JSONArray
	 */
	public JSONArray bucketsLine(String param, String dateTime, String apiKey, String type);

	public JSONArray bucketsPie(String param, String dateTime, String apiKey, String type);

}
