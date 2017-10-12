package cn.ce.platform_service.openApply.dao;

import java.util.List;

import cn.ce.platform_service.openApply.entity.OpenApplyEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月11日
*/
public interface INewOpenApplyDao {


	OpenApplyEntity save(OpenApplyEntity apply);

	OpenApplyEntity findOpenApplyById(String id);

	List<OpenApplyEntity> findOpenApplyByNameOrKey(String applyName, String applyKey);


}
