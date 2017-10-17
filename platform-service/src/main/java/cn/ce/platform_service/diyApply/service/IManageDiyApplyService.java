package cn.ce.platform_service.diyApply.service;

import java.util.List;

import javax.annotation.Resource;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.dao.IDiyApplyDao;
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

	public Result<String> batchUpdate(List<String> ids);

	public Result<String> auditUpdate(String id, int checkState, String checkMem);
	
	public Result<Apps> findPagedApps(String owner, String name, int pageNum, int pageSize);

	public Result<InterfaMessageInfoString> registerBathApp(String tenantId, String apps);
	
	/**
	 * 
	 * @param owner
	 *            所属企业 CE 为中企动力 不填为所有
	 * @param name
	 *            名称 模糊搜索 不填为所有
	 * @param pageNum
	 *            当前第几页
	 * @param pageSize
	 *            每页显示多少条
	 * @return
	 */
	

	public Result<InterfaMessageInfoString> saveOrUpdateApps(String apps);

}
