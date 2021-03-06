package cn.ce.platform_service.apis.dao;

import java.util.List;
import java.util.Map;

import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.apis.entity.QueryApiEntity;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.users.entity.User;

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

	Page<ApiEntity> findManagerListWithoutPage(QueryApiEntity apiEntity);
	
	ApiEntity findOneByFields(Map<String, Object> map);

	List<ApiEntity> findApiByIds(List<String> apiId);
	
	List<ApiEntity> findApiByApplyIdsAndCheckState(List<String> openApplyIds, Integer checkState);
	
	List<ApiEntity> findApiByApplyIdsAndCheckState(List<String> openApplyIds, Integer checkState, String apiType);

	List<ApiEntity> findByField(String key, String value);

	Page<ApiEntity> findApiPageByIds(List<String> apiIds, Page<ApiEntity> page);

	ApiEntity findByVersion(String versionId, String version);

	Page<ApiEntity> findApiPageByIdsAndNameLike(List<String> apiIds, String apiName, Page<ApiEntity> page);

	List<ApiEntity> findApiByIds(List<String> apiIds, int checkState);

	List<ApiEntity> findByIdsOrAppIds(List<String> apiIds, List<String> appIds);

	void deleteApis(List<String> successApiIds);

	List<ApiEntity> findApiByCheckState(int checkState);

	List<ApiEntity> findAll();

}
