package cn.ce.es.service.Impl;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

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

import cn.ce.es.bean.KeyValueForDate;
import cn.ce.es.bean.LineData;
import cn.ce.es.service.IStatsService;
import cn.ce.es.util.ElasticsearchUtils;
import cn.ce.es.util.SplitDateUtil;
import cn.ce.platform_service.apis.dao.IMysqlApiDao;
import cn.ce.platform_service.apis.entity.NewApiEntity;
import cn.ce.platform_service.common.Result;
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
@Service("statsService")
public class StatsServiceImpl implements IStatsService {

	private static Logger logger = Logger.getLogger(StatsServiceImpl.class);

	public String indexName = "tyk_analytics";
	public String typeName = "tyk_analytics";

	@Autowired
	public ElasticsearchUtils es;

	@Resource
	private IMysqlApiDao mysqlApiDao;

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
		MatchPhraseQueryBuilder mpbv = null;
		String from = SplitDateUtil.returnForwadTime(dateTime, type)[0];

		String to = SplitDateUtil.returnForwadTime(dateTime, type)[1];

		listTime = SplitDateUtil.getKeyValueForDate(from, to);

		NewApiEntity nae = this.mysqlApiDao.findById(apiKey);
		String tempkey = nae.getVersionId();
		String version = nae.getVersion();

		if ("2".equals(param)) {
			mpb = QueryBuilders.matchPhraseQuery("api_id", tempkey);
			mpbv = QueryBuilders.matchPhraseQuery("api_version", version);
		} else {
			mpb = QueryBuilders.matchPhraseQuery("api_key", apiKey);
		}

		queryBuilder = QueryBuilders.boolQuery().must(mpb).must(mpbv)
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
		MatchPhraseQueryBuilder mpbv = null;
		NewApiEntity nae = this.mysqlApiDao.findById(apiKey);
		String tempkey = nae.getVersionId();
		String version = nae.getVersion();

		// api 提供者
		if ("2".equals(param)) {

			mpb = QueryBuilders.matchPhraseQuery("api_id", tempkey);
			mpbv = QueryBuilders.matchPhraseQuery("api_version", version);

		} else {
			mpb = QueryBuilders.matchPhraseQuery("api_key", apiKey);
		}

		queryBuilder = QueryBuilders.boolQuery().must(mpb).must(mpbv)
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

	// public static void main(String[] args) {
	// ElasticsearchUtils es = new ElasticsearchUtils("my-application",
	// "127.0.0.1");
	// StatsServiceImpl s = new StatsServiceImpl();
	// Date da = new Date();
	// System.out.println(da.getTime());
	// s.bucketsLine("2", "1504233289987", "c835da3e79ae45858fe6ed9315688989", "3");
	// s.bucketsPie("2", "1504233289987", "c835da3e79ae45858fe6ed9315688989", "3");
	// s.es.closeClient();
	// }

	@Override
	public Result<String> getReport(String param, String dateTime, String apiKey, String type) {

		Result<String> result = new Result<>();
		JSONObject data = new JSONObject();
		try {
			data.put("bucketsLine", bucketsLine(param, dateTime, apiKey, type));
			data.put("bucketsPie", bucketsPie(param, dateTime, apiKey, type));
			result.setSuccessData(data.toString());
		} catch (Exception e) {
			result.setErrorMessage("查询统计图失败,请联系管理员!");
		}

		return result;
	}

}
