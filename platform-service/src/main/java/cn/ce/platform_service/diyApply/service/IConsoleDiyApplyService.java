package cn.ce.platform_service.diyApply.service;

import java.util.List;

import org.json.JSONObject;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;
import cn.ce.platform_service.diyApply.entity.tenantAppsEntity.TenantApps;

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
	Result<String> saveApply(DiyApplyEntity entity);

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

	/***
	 * 根据条件查询应用列表
	 * 
	 * @param query
	 * @return
	 */
	Result<List<DiyApplyEntity>> findApplyList(DiyApplyEntity entity);

	/**
	 * 根据id加载应用信息
	 * 
	 * @param id
	 * @return
	 */
	Result<DiyApplyEntity> getApplyById(String id, int pageSize, int currentPage);

	/***
	 * 
	 * @Title: findById @Description: 根据id加载应用信息 @param : @param applyId @param
	 *         : @return @return: ApplyEntity @throws
	 */
	public DiyApplyEntity findById(String applyId);

	/**
	 * @Description 根据key获取产品实例
	 * @param key
	 * @return 产品实例
	 */
	public Result<TenantApps> findTenantAppsByTenantKey(String key);

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
	public Result<Apps> findPagedApps(String owner, String name, int pageNum, int pageSize);

	public Result<InterfaMessageInfoString> registerBathApp(String tenantId, String apps);

	public Result<InterfaMessageInfoString> saveOrUpdateApps(String apps);

	public Result<InterfaMessageInfoString> generatorTenantKey(String id);

	public Result<String> auditUpdate(String id);

	public Result<String> batchUpdate(String ids);
	
	
	/**
	 * 
	 * @Title: productMenuList
	 * @Description: 获取产品菜单列表
	 * @param : @param bossInstanceCode
	 * @param : @return
	 * @return: Result<InterfaMessageInfoJasonObject>
	 * @throws
	 */
	public Result<String> productMenuList(String bossInstanceCode);
	
	/**
	 * 
	 * @Title: registerMenu
	 * @Description: 注册菜单
	 * @param : @param appid
	 * @param : @param bossInstanceCode
	 * @param : @param menuJson
	 * @param : @return
	 * @return: Result<InterfaMessageInfoJasonObject>
	 * @throws
	 */
	public Result<String> registerMenu(String appid,String bossInstanceCode,String menuJson);

}
