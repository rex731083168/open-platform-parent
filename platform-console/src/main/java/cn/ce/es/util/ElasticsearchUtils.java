package cn.ce.es.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.date.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

import cn.ce.es.bean.KeyValueForDate;
import cn.ce.es.bean.LineData;

import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;

public class ElasticsearchUtils {

	private static Logger logger = Logger.getLogger(ElasticsearchUtils.class);
	private static TransportClient client;

	@SuppressWarnings("resource")
	public ElasticsearchUtils(String clusterName, String ipAddress) {
		long start = System.currentTimeMillis();

		Settings settings = Settings.builder().put("cluster.name", clusterName).build();

		try {
			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipAddress), 9300));

			long end = System.currentTimeMillis();
			System.out.println(end - start + "ms" + "+getclient时间");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return TransportClient
	 */
	@SuppressWarnings("static-access")
	public TransportClient getclient(String clusterName, String ipAddress) {

		return this.client;
	}

	/**
	 * 
	 */
	@SuppressWarnings("static-access")
	public void closeClient() {
		this.client.close();
	}

	/**
	 * DateRanges聚合方法
	 * 
	 * @param indexName
	 * @param typeName
	 * @param queryBuilder
	 * @param daterangfiledname
	 *            按时间聚合的filed
	 * @param listTime
	 *            not nul最少包含一个组装的KeyValueForDate对象
	 * @return List<Range.Bucket>key & doccount
	 */
	public List<LineData> getDateRangesAggregation(String indexName, String typeName, String daterangfiledname,
			List<KeyValueForDate> listTime, QueryBuilder queryBuilder) {

		DateRangeAggregationBuilder daggbuilder = AggregationBuilders.dateRange("by_@time").field(daterangfiledname)
				.format("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSZ");

		Iterator<KeyValueForDate> iterator = listTime.iterator();

		daggbuilder.timeZone(DateTimeZone.getDefault());
		daggbuilder.subAggregation(AggregationBuilders.avg("aggavg").field("request_time_ms"));

		/*
		 * 组装daggbuilder的时间区间daterang
		 */
		int count = 0;
		while (iterator.hasNext()) {
			count++;
			KeyValueForDate temp = (KeyValueForDate) iterator.next();

			if (count == 1) {
				daggbuilder.addUnboundedTo(temp.getStartDate());
			} else {
				daggbuilder.addRange(temp.getStartDate(), temp.getEndDate());
				if (!iterator.hasNext()) {// ���һ��Ԫ��
					daggbuilder.addUnboundedFrom(temp.getEndDate());
				}
			}

		}

		SearchResponse searchResponse = client.prepareSearch(indexName).setTypes(typeName).setQuery(queryBuilder)
				.addAggregation(daggbuilder).setExplain(true).execute().actionGet();
		Range ra = searchResponse.getAggregations().get("by_@time");

		@SuppressWarnings("unchecked")
		List<Range.Bucket> bucklist = (List<Range.Bucket>) ra.getBuckets();
		List<LineData> bucklistLidata = new ArrayList<LineData>();

		for (Range.Bucket b : bucklist) {
			LineData ld = new LineData();
			ld.setKeyAsString(b.getKeyAsString());
			ld.setDoccount(b.getDocCount());
			InternalAvg intavg = b.getAggregations().get("aggavg");
			if (String.valueOf(intavg.getValue()) == "NaN") {
				ld.setIntavg(0.0);
			} else {
				ld.setIntavg(intavg.getValue());
			}
			bucklistLidata.add(ld);

		}

		logger.info(ra.getBuckets().size() + "Rangescount");

		return bucklistLidata;

	}

	/**
	 * Terms聚合方法
	 * 
	 * @param indexName
	 * @param typeName
	 * @param termsFiledName
	 *            聚合的字段名称
	 * @param queryBuilder
	 *            结果必须包含termsFiledName
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public List getTermsAggregation(String indexName, String typeName, String termsFiledName,
			QueryBuilder queryBuilder) {

		TermsAggregationBuilder ta = AggregationBuilders.terms("genders").field(termsFiledName);
		SearchResponse searchResponse = client.prepareSearch(indexName).setTypes(typeName).setQuery(queryBuilder)
				.addAggregation(ta).setExplain(true).execute().actionGet();
		Terms ra = searchResponse.getAggregations().get("genders");
		logger.info(ra.getBuckets().size() + "Termscount");
		return ra.getBuckets();

	}

}
