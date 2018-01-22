package cn.ce.platform_service.diyApply.service;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;

/***
 * 
 * 应用服务接口
 * 
 * @author lida
 * @date 2017年8月23日14:32:35
 *
 */
public interface IConsoleDiyApplyService {

	/***
	 * 更新/添加实体
	 * 
	 * @param entity
	 */
	Result<?> saveApply(DiyApplyEntity entity);

	/***
	 * 根据id删除实体
	 * 
	 * @param id
	 */
	Result<String> deleteApplyByid(String id);

	/***
	 * 根据条件分页查询应用列表
	 * 
	 * @param query
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	Result<Page<DiyApplyEntity>> findApplyList(DiyApplyEntity entity, Page<DiyApplyEntity> page);


	/**
	 * 根据id加载应用信息
	 * 
	 * @param id
	 * @return
	 */
	//Result<DiyApplyEntity> getApplyById(String id, int pageSize, int currentPage);

	/***
	 * 
	 * @Title: findById @Description: 根据id加载应用信息 @param : @param applyId @param
	 *         : @return @return: ApplyEntity @throws
	 */
	public DiyApplyEntity findById(String applyId);

	
	public Result<InterfaMessageInfoString> generatorTenantKey(String id);


	public Result<String> batchUpdate(String ids,int checkState, String checkMem);

	/**
	 * 
	 * @Title: productMenuList @Description: 获取产品菜单列表 @param : @param
	 * bossInstanceCode @param : @return @return:
	 * Result<InterfaMessageInfoJasonObject> @throws
	 */
	public Result<String> productMenuList(String bossInstanceCode);

	/**
	 * 
	 * @Title: registerMenu @Description: 注册菜单 @param : @param appid @param : @param
	 * bossInstanceCode @param : @param menuJson @param : @return @return:
	 * Result<InterfaMessageInfoJasonObject> @throws
	 */
	public Result<String> registerMenu(String appid, String bossInstanceCode, String menuJson);

	Result<?> updateApply(DiyApplyEntity apply);

	Result<?> migraDiyApply();

	
}
