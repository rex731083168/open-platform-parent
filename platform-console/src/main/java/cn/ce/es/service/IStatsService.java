package cn.ce.es.service;

import cn.ce.platform_service.common.Result;
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
public interface IStatsService {
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

	
	public Result<String> getReport(String param, String dateTime, String apiKey, String type);
	
}
