package cn.ce.platform_service.diyApply.service;

import java.util.List;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;

/**
 * @Description : 说明
 * @Author : makangwei
 * @Date : 2017年10月16日
 */
public interface IManageDiyApplyService {

	public Result<Page<DiyApplyEntity>> findPagedApps(String productName, String userName, Integer checkState,
			String applyName, int currentPage, int pageSize);

	public Result<String> batchUpdate(List<String> ids, Integer checkState, String checkMem);


	public Result<InterfaMessageInfoString> registerBathApp(String tenantId, String apps);

	/***
	 * 
	 * @Title: findById @Description: 根据id加载应用信息 @param : @param applyId @param
	 *         : @return @return: ApplyEntity @throws
	 */
	public Result<DiyApplyEntity> findById(String applyId);
	
	

}
