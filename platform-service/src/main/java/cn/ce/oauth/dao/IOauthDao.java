package cn.ce.oauth.dao;

import java.util.Map;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年8月22日
*/
public interface IOauthDao {

	int findByFields(Map<String, Object> queryMap);

}
