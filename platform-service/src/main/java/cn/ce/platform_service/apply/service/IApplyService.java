package cn.ce.apply.service;

import java.util.List;

import cn.ce.apply.entity.ApplyEntity;
import cn.ce.common.Result;
import cn.ce.page.Page;

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
	Result<Page<ApplyEntity>> findApplyList(ApplyEntity entity,int currentPage,int pageSize);
	
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

	ApplyEntity findById(String applyId);
	
}
