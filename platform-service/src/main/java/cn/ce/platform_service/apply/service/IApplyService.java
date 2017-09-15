package cn.ce.platform_service.apply.service;

import java.util.List;

import cn.ce.platform_service.apply.entity.ApplyEntity;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.page.Page;

/***
 * 
 * 应用服务接口
 * @author lida
 * @date 2017年8月23日14:32:35
 *
 */
public interface IApplyService {

	/***
	 * 更新/添加实体
	 * @param entity
	 */
	Result<String> saveApply(ApplyEntity entity);
	
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
	Result<Page<ApplyEntity>> findApplyList(ApplyEntity entity,Page<ApplyEntity> page);
	
	/***
	 * 根据条件查询应用列表
	 * @param query
	 * @return
	 */
	Result<List<ApplyEntity>> findApplyList(ApplyEntity entity);
	
	/**
	 * 根据id加载应用信息
	 * @param id
	 * @return
	 */
	Result<ApplyEntity> getApplyById(String id,int pageSize,int currentPage);
	
	/***
	 * 
	 * @Title: findById
	 * @Description: 根据id加载应用信息
	 * @param : @param applyId
	 * @param : @return
	 * @return: ApplyEntity
	 * @throws
	 */
	public ApplyEntity findById(String applyId);
	
}
