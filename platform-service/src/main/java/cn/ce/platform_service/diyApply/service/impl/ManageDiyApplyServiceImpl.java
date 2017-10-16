package cn.ce.platform_service.diyApply.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.diyApply.dao.IDiyApplyDao;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.service.IManageDiyApplyService;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月16日
*/
@Service
public class ManageDiyApplyServiceImpl implements IManageDiyApplyService{
	@Resource
	private IDiyApplyDao diyApplyDao;
	@Override
	public Result<Apps> findPagedApps(String owner, String name, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Result<String> batchUpdate(List<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}


}
