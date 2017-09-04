package cn.ce.stats.service.impl;

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
import cn.ce.stats.service.StatsService;
import cn.ce.stats.util.ElasticsearchUtils;
import cn.ce.stats.util.SplitDateUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @Title: StatsServiceImpl.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年9月1日 time下午4:16:39
 *
 **/
@Service
public class StatsServiceImpl implements StatsService{

	private static Logger logger = Logger.getLogger(StatsServiceImpl.class);

	public String indexName = "tyk_analytics";
	public String typeName = "tyk_analytics";

	@Autowired
	public ElasticsearchUtils es;

	/**
	 * 
	 * @param param
	 * @param dateTime
	 * @param apiKey
	 * @param type
	 * @return JSONArray
	 */
	public JSONArray bucketsLine(String param, String dateTime, String apiKey, String type) {

		long start = System.currentTimeMillis();
		JSONArray jsa = new JSONArray();
		List<KeyValueForDate> listTime = null;
		QueryBuilder queryBuilder = null;
		List<LineData> bucklist = null;
		MatchPhraseQueryBuilder mpb = null;
		String from = SplitDateUtil.returnForwadTime(dateTime, type)[0];

		String to = SplitDateUtil.returnForwadTime(dateTime, type)[1];

		listTime = SplitDateUtil.getKeyValueForDate(from, to);

		if ("2".equals(param)) {
			mpb = QueryBuilders.matchPhraseQuery("api_id", apiKey);
		} else {
			mpb = QueryBuilders.matchPhraseQuery("api_key", apiKey);
		}

		queryBuilder = QueryBuilders.boolQuery().must(mpb)
				.must(QueryBuilders.rangeQuery("@timestamp").from(from).to(to));
		bucklist = es.getDateRangesAggregation(indexName, typeName, "@timestamp", listTime, queryBuilder);
		jsa = new JSONArray(bucklist);
		long end = System.currentTimeMillis();
		logger.info(end - start + "ms" + "线图执行查询时间");
		return jsa;
	}

	public JSONArray bucketsPie(String param, String dateTime, String apiKey, String type) {
		long start = System.currentTimeMillis();
		JSONArray bucketsPie = new JSONArray();

		String from = SplitDateUtil.returnForwadTime(dateTime, type)[0];

		String to = SplitDateUtil.returnForwadTime(dateTime, type)[1];

		TermsAggregationBuilder ta = AggregationBuilders.terms("taterms").field("response_code");
		QueryBuilder queryBuilder = null;
		MatchPhraseQueryBuilder mpb = null;

		// api 提供者
		if ("2".equals(param)) {

			mpb = QueryBuilders.matchPhraseQuery("api_id", apiKey);

		} else {
			mpb = QueryBuilders.matchPhraseQuery("api_key", apiKey);
		}

		queryBuilder = QueryBuilders.boolQuery().must(mpb)
				.must(QueryBuilders.rangeQuery("@timestamp").from(from).to(to));
		List<LongTerms> bucklist = es.getTermsAggregation(indexName, typeName, "response_code", queryBuilder);
		Iterator bi = bucklist.iterator();
		while (bi.hasNext()) {
			Terms.Bucket lt = (Terms.Bucket) bi.next();
			JSONObject object = new JSONObject();
			object.put("key", lt.getKeyAsString());
			object.put("value", lt.getDocCount());
			bucketsPie.put(object);
		}

		long end = System.currentTimeMillis();
		logger.info(end - start + "ms" + "饼图执行查询时间");
		return bucketsPie;

	}

	public static void main(String[] args) {
		ElasticsearchUtils es = new ElasticsearchUtils("my-application", "127.0.0.1");
		StatsServiceImpl s = new StatsServiceImpl();
		Date da = new Date();
		System.out.println(da.getTime());
		s.bucketsLine("2", "1504233289987", "c835da3e79ae45858fe6ed9315688989", "3");
		s.bucketsPie("2", "1504233289987", "c835da3e79ae45858fe6ed9315688989", "3");
		s.es.closeClient();
	}

}
