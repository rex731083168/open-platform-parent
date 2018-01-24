package cn.ce.platform_service.apis.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.apis.entity.ApiResultExampleEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月19日
*/
@Transactional(propagation=Propagation.REQUIRED)
public interface IMysqlApiRexampleDao {

	int save(ApiResultExampleEntity re);

	ApiResultExampleEntity findOneByApiId(String apiId);

	int deleteByApiId(String apiId);

}
