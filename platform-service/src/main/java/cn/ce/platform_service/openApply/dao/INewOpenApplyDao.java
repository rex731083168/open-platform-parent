package cn.ce.platform_service.openApply.dao;

import java.util.List;
import java.util.Map;

import cn.ce.platform_service.core.bean.MongoDBWhereEntity;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;
import cn.ce.platform_service.page.Page;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月11日
*/
public interface INewOpenApplyDao {


	OpenApplyEntity save(OpenApplyEntity apply);
	
	void batchSaveApply(List<String> ids,Integer checkState);

	OpenApplyEntity findOpenApplyById(String id);

	List<OpenApplyEntity> findOpenApplyByNameOrKey(String applyName, String applyKey);

	List<OpenApplyEntity> findOpenApplyByEntity(Map<String,MongoDBWhereEntity> whereEntity);
	
	Page<OpenApplyEntity> findOpenApplyByEntity(Map<String,MongoDBWhereEntity> whereEntity,Page<OpenApplyEntity> page);
	
	List<OpenApplyEntity> findOpenApplyByNeqId(OpenApplyEntity apply);

}
