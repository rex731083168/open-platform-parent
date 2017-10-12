package cn.ce.platform_service.apis.dao;

import java.util.Map;

import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.common.page.Page;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月12日
*/
public interface INewApiDao {

	ApiEntity save(ApiEntity apiEntity);

	int updApiVersionByApiId(String apiId);

	ApiEntity findApiById(String apiId);

	Page<ApiEntity> findSupplierApis(String openApplyId, String userId, String apiNameLike, Integer checkState,
			int currentPage, int pageSize);

	ApiEntity findOneByFields(Map<String, Object> map);

}
