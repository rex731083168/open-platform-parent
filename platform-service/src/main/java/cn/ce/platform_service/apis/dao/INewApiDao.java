package cn.ce.platform_service.apis.dao;

import java.util.List;
import java.util.Map;

import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.apis.entity.QueryApiEntity;
import cn.ce.platform_service.common.page.Page;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月12日
*/
public interface INewApiDao {

	ApiEntity save(ApiEntity apiEntity);

	int updApiVersionByApiId(String apiId, boolean flag);

	ApiEntity findApiById(String apiId);

	Page<ApiEntity> findSupplierApis(QueryApiEntity entity,int currentPage, int pageSize);
	
	Page<ApiEntity> findManagerList(QueryApiEntity entity,int currentPage, int pageSize);

	ApiEntity findOneByFields(Map<String, Object> map);

	List<ApiEntity> findApiByIds(List<String> apiId);

	List<ApiEntity> findByField(String apisApiversionVersionid, String versionId);


}
