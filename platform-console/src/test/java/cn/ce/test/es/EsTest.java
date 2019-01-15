package cn.ce.test.es;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * @ClassName: EsTest
 * @Description: TODO
 * @create 2019/1/8 12:00/MKW
 * @update 2019/1/8 12:00/MKW/(说明。)....多次修改添加多个update
 */
public class EsTest {
    private static TransportClient client;

    static void getClient(String ipAddress, String clusterName){

        Settings settings = Settings.builder().put("cluster.name", clusterName).build();

        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipAddress), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        getClient("10.12.40.161","open-platform");
//        client.prepareIndex("tyk_analytics", "tyk_analytics");
        Date startTime = new Date(946656000000L); //2000年
        Date endTime = new Date(253370736000000L); //9999年
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("@timestamp").gt(startTime).lt(endTime);
        SearchResponse response = client.prepareSearch("tyk_analytics")
                .setTypes("tyk_analytics")
                .setQuery(rangeQueryBuilder)
                .setSize(0)
                .get();
        long num = response.getHits().getTotalHits();
        System.out.println(num);
    }
}
