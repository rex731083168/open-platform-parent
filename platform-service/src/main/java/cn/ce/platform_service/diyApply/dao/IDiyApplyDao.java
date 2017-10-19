package cn.ce.platform_service.diyApply.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;

import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;

/***
 * 应用dao 
 * @author lida
 * @date 2017年8月23日14:15:12
 *
 */
public interface IDiyApplyDao {

	/***
	 * 更新/添加实体
	 * @param entity
	 */
	void saveOrUpdate(DiyApplyEntity entity);
	
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
	Page<DiyApplyEntity> findPageByEntity(Query query,Page<DiyApplyEntity> page);
	
	/***
	 * 根据条件查询应用列表
	 * @param query
	 * @return
	 */
	List<DiyApplyEntity> findListByEntity(Query query);
	
	/**
	 * 根据id加载应用信息
	 * @param id
	 * @return
	 */
	DiyApplyEntity findById(String id);
	
	/**
	 * 批量審核
	 * @param ids
	 * @return
	 */
	public String bathUpdateByid(List<String> ids,int checkState,String checkMem) ;
	
	
	/**
	 * 批量審核并发布
	 * @param ids 
	 * @return
	 */
	public String bathUpdateByidAndPush(List<String> ids,Map<String, Object> map,int checkState,String checkMem) ;

	/**
	 * @Title: findApplyList
	 * @Description: 根据部分参数查询定制应用
	 * @author: makangwei 
	 * @date:   2017年10月19日 上午10:36:35 
	 */
	Page<DiyApplyEntity> findApplyList(String applyName, String productName, Integer checkState,
			Page<DiyApplyEntity> page);
}
