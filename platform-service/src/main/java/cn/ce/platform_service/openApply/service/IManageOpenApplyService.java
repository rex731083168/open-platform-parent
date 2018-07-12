package cn.ce.platform_service.openApply.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.entity.interfaceMessageInfo.InterfaMessageInfoString;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;
import cn.ce.platform_service.openApply.entity.QueryOpenApplyEntity;
import net.sf.json.JSONObject;

/**
 * 
 * @ClassName: IAppService
 * @Description: 服务分组
 * @author dingjia@300.cn
 *
 */
public interface IManageOpenApplyService {

	/**
	 * @Description: 根据应用Id查询应用信息 @param id: 应用id @return 返回应用对象 @throws
	 */
	public Result<OpenApplyEntity> findById(String id);

	/**
	 * @Description: 修改应用信息 @param app: 应用对象 @throws
	 */
//	public void modifyById(OpenApplyEntity app);

	/**
	 * @Description: 查询所有应用 @return 返回应用集合 @throws
	 */
//	public List<OpenApplyEntity> getAll();

	/**
	 * @Description: 根据用户查询应用列表（翻页） @param userId: 分组ID @param currentPage:
	 *               当前页 @param pageSize: 每页显示数量 @return 返回应用集合 @throws
	 */
//	public Page<OpenApplyEntity> getAppList(String userId, int currentPage, int pageSize);

	/**
	 * @Description: 根据App实体对象查询应用列表（翻页） @param appEntity: 服务分类实体 @param
	 *               currentPage: 当前页 @param pageSize: 每页显示数量 @return 返回应用集合 @throws
	 */
	// public Page<AppEntity> getAppListByDBWhere(Map<String,MongoDBWhereEntity>
	// condition, int currentPage, int pageSize);
//	public Page<OpenApplyEntity> getAppListByDBWhere(OpenApplyEntity appentity, int currentPage, int pageSize);

	/**
	 * @Description: 删除应用 @param id: 应用id @throws
	 */
//	public void delById(String id);

//	public OpenApplyEntity findAppByAppName(String appName);

	/***
	 * 根据实体对象分页查询App集合
	 * 
	 * @param entity
	 *            查询的实体对象
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
//	Page<OpenApplyEntity> findAppsByEntity(OpenApplyEntity entity, int currentPage, int pageSize);

	/***
	 * 根据app实体中appName及appKey检测是否已经存在
	 * 
	 * @param entity
	 *            App实体
	 * @return
	 */
//	Result<OpenApplyEntity> checkAppIsHave(OpenApplyEntity entity);

//	List<OpenApplyEntity> findAppsByEntity(OpenApplyEntity entity);

//	public Result<String> deleteById(String appId);

//	public Result<JSONObject> appList(HttpServletRequest request, HttpServletResponse response);

	// public Result<String> addGroup(HttpSession session, OpenApplyEntity app);

//	public Result<String> delGroup(String appId);

	// public Result<String> modifyGroup(OpenApplyEntity app);

//	public Result<Page<OpenApplyEntity>> groupList(String userId, int currentPage, int pageSize);

//	public Result<String> submitVerify(String id);

//	public Result<String> addGroup1(HttpSession session, OpenApplyEntity app);

//	public Result<String> deleteGroup(String id);

//	public Result<String> modifyGroup1(OpenApplyEntity app);

	// public Result<Page<OpenApplyEntity>> groupList1(String appName, String
	// userName, String enterpriseName,
	// String checkState, int currentPage, int pageSize);

	public Result<Page<OpenApplyEntity>> findOpenApplyList(QueryOpenApplyEntity queryEntity);

	public Result<String> batchUpdate(String sourceConfig, List<String> ids, Integer checkState, String batchUpdate);
	// public Result<String> modifyGroup1(OpenApplyEntity app);

//	public Result<Page<OpenApplyEntity>> groupList1(String appName, String checkState, int currentPage, int pageSize);

//	public Result<InterfaMessageInfoString> saveOrUpdateApps(String apps);

}
