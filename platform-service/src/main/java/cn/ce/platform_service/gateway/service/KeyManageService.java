package cn.ce.platform_service.gateway.service;
//package cn.ce.gateway.service;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import org.springframework.stereotype.Service;
//
//import cn.ce.common.Result;
//import cn.ce.gateway.entity.GatewayKeyEntity;
//
///**
// * @Description : 说明
// * @Author : makangwei
// * @Date : 2017-8-9
// */
//@Service
//public class KeyManageService {
//
////	public Result<String> applyKey(String userId, String apiId) {
////		
////		String secretKey = UUID.randomUUID().toString().replace("-", "");
////		
////		Date createDate = new Date();
////		
////		//保存到数据库key表中
////		KeyEntity keyEntity = new KeyEntity(secretKey,userId,"userName",createDate,apiId,status);
////		
////		
////		return null;
////		
////	}
//
//	public Result<String> allowKey(String secretKey) {
//		
//		//1、根据secretKey查询KeyEntity
//		KeyEntity entity = keyDao.getKeyBySecret(secretKey);
//		//2、根据KeyEntity中的apiId查询api
//		String apiId = entity.getApiId();
//		ApiEntity apiEntity = apiDao.getApiById(apiId);
//		//3、封装json
//		GatewayKeyEntity gwKeyEntity = new GatewayKeyEntity();
//		gwKeyEntity.setPer();
//		gwKeyEntity.setRate();
//		gwKeyEntity.setQuota_max();
//		gwKeyEntity.setQuota_renewal_rate();
//		
//		JSONArray innerArray = new JSONArray();
//		innerArray.put("Default");
//		
//		JSONObject innerJson = new JSONObject();
//		innerJson.put("api_name", );
//		innerJson.put("api_id", );
//		innerJson.put("versions", innerArray);
//		
//		JSONObject job = new JSONObject();
//		job.put(apiEntity.getApiId(), job);
//		
//		gwKeyEntity.setAccess_rights(job);
//		
//		
//		//4、修改网关
//		
//		
//		
//		//5、修改数据库中key的状态
//		
//		
//		return null;
//		
//	}
//	
//	public Result<String> deleteKey(String secretKey){
//		
//		//1、删除网关上的key
//		
//		//2、删除数据库中的key
//	}
//
//}
////{
////    "org_id": "default",
////    //org_id，组织ID，String类型
////    "expires": -1,
////    //expires，Integer类型，密钥过期时间（时间戳）
////    "rate": 100,
////    //rate，Integer类型，访问频次
////    "per": 10,
////    //per，Integer类型，访问频次的计数周期（单位秒）
////    "quota_max": 1000,
////    //quota_max，Integer类型，访问最大配额
////    "quota_renewal_rate": 3600,
////    //quota_renewal_rate，Integer类型，访问最大配额的重置周期（单位秒）
////    "quota_remaining": 1000,
////    //quota_remaining，Integer类型，当前剩余访问最大配额
////    "quota_renews": 0,
////    //quota_renews，Integer类型，访问最大配额的重置时间戳（由Tyk自动填充）
////    "is_inactive": false,
////    //is_inactive，Boolean类型，密钥是否可用
////    "access_rights": {
////    //access_rights，json类型，权限列表
////           "2fdd8512a856434a61f080da67a88852": {
////           //API_ID
////                  "api_name": "test2",
////                  //api_name，API名称
////                  "api_id": "2fdd8512a856434a61f080da67a88852",
////                  //api_id，API_ID
////                  "versions": [
////                         //versions，允许的版本列表
////                         "Default"
////                  ]
////           }
////    }
////}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
