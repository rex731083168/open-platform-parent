package cn.ce.platform_service.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import cn.ce.platform_service.apis.entity.ApiEntity;

/**
* @Description : mongodb连接工具类
* @Author : makangwei
* @Date : 2017年12月6日
*/
public class MongoConnectUtils {

	public static void main(String[] args) {
		List<ServerAddress> list1 = new ArrayList<ServerAddress>();
		list1.add(new ServerAddress("10.12.40.83",23000));
		list1.add(new ServerAddress("10.12.40.84",23000));
		list1.add(new ServerAddress("10.12.40.86",23000));
		List<MongoCredential> list2 = new ArrayList<MongoCredential>();
		list2.add(MongoCredential.createCredential("makangwei", "admin","123456".toCharArray()));
		MongoClient client1 = new MongoClient(list1,list2);
		MongoOperations mongoOps = new MongoTemplate(client1,"openplatform");
		List<ApiEntity> list = mongoOps.findAll(ApiEntity.class);
		System.out.println(list.size());
	}
}
