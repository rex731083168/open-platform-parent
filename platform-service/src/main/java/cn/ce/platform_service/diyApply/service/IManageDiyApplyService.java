package cn.ce.platform_service.diyApply.service;

import java.util.List;

import javax.annotation.Resource;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.diyApply.dao.IDiyApplyDao;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月16日
*/
public interface IManageDiyApplyService {
	

	public Result<Apps> findPagedApps(String owner, String name, int pageNum, int pageSize);
	public Result<String> batchUpdate(List<String> ids);

}
