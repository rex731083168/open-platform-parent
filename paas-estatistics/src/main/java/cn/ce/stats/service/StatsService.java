package cn.ce.stats.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ce.stats.entity.KeyValueForDate;
import cn.ce.stats.entity.LineData;
import cn.ce.stats.util.ElasticsearchUtils;
import cn.ce.stats.util.SplitDateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @Title: StatsService.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年9月1日 time下午1:45:06
 *
 **/
@Service
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
