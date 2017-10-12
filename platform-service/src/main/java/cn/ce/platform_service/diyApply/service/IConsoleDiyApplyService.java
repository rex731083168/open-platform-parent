package cn.ce.platform_service.diyApply.service;

import java.util.List;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;

/***
 * 
 * 应用服务接口
 * @author lida
 * @date 2017年8月23日14:32:35
 *
 */
public interface IConsoleDiyApplyService {

	/***
	 * 更新/添加实体
	 * @param entity
	 */
	Result<String> saveApply(DiyApplyEntity entity);
	
	/***
	 * 根据id删除实体
	 * @param id
	 */
	Result<String> deleteApplyByid(String id);
	
	/***
	 * 根据条件分页查询应用列表
	 * @param query
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	Result<Page<DiyApplyEntity>> findApplyList(DiyApplyEntity entity,Page<DiyApplyEntity> page);
	
	/***
	 * 根据条件查询应用列表
	 * @param query
	 * @return
	 */
	Result<List<DiyApplyEntity>> findApplyList(DiyApplyEntity entity);
	
	/**
	 * 根据id加载应用信息
	 * @param id
	 * @return
	 */
	Result<DiyApplyEntity> getApplyById(String id,int pageSize,int currentPage);
	
	/***
	 * 
	 * @Title: findById
	 * @Description: 根据id加载应用信息
	 * @param : @param applyId
	 * @param : @return
	 * @return: ApplyEntity
	 * @throws
	 */
	public DiyApplyEntity findById(String applyId);
	
}
