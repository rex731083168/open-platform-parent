package cn.ce.platform_service.diyApply.service;

import java.util.List;

import javax.annotation.Resource;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;

/**
 * @Description : 说明
 * @Author : makangwei
 * @Date : 2017年10月16日
 */
public interface IManageDiyApplyService {

	public Result<Page<DiyApplyEntity>> findPagedApps(String productName, String userName, int checkState,
			String applyName, int currentPage, int pageSize);

	public Result<String> batchUpdate(List<String> ids, Integer checkState, String checkMem);

	public Result<Apps> findPagedApps(String owner, String name, int pageNum, int pageSize);

	public Result<InterfaMessageInfoString> registerBathApp(String tenantId, String apps);

}
