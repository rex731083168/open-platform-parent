package cn.ce.apply.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import cn.ce.apply.entity.ApplyEntity;
import cn.ce.page.Page;

/***
 * 应用dao 
 * @author lida
 * @date 2017年8月23日14:15:12
 *
 */
public interface IApplyDao {

	/***
	 * 更新/添加实体
	 * @param entity
	 */
	void saveOrUpdate(ApplyEntity entity);
	
	/***
	 * 根据id删除实体
	 * @param id
	 */
	void delete(String id);
	
	/***
	 * 根据条件分页查询应用列表
	 * @param query
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	Page<ApplyEntity> findPageByEntity(Query query,int currentPage,int pageSize);
	
	/***
	 * 根据条件查询应用列表
	 * @param query
	 * @return
	 */
	List<ApplyEntity> findListByEntity(Query query);
	
	/**
	 * 根据id加载应用信息
	 * @param id
	 * @return
	 */
	ApplyEntity getApplyById(String id);

	ApplyEntity findById(String applyId);
}
