package cn.ce.platform_service.apis.dao;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.apis.entity.ApiArgEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月19日
*/
@Transactional(propagation=Propagation.REQUIRED)
public interface IMysqlApiArgDao {

	int save(ApiArgEntity arg);

	List<ApiArgEntity> findByApiId(String apiId);

	int deleteByApiId(String apiId);

}
