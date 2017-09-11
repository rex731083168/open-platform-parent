package cn.ce.apply.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.ce.apis.entity.ApiAuditEntity;
import cn.ce.apis.service.IAPIService;
import cn.ce.apis.service.IApiOauthService;
import cn.ce.apply.dao.IApplyDao;
import cn.ce.apply.entity.ApplyEntity;
import cn.ce.apply.service.IApplyService;
import cn.ce.common.Result;
import cn.ce.common.Status;
import cn.ce.page.Page;

/***
 * 
 * 应用服务接口实现类
 * @author lida
 * @date 2017年8月23日14:32:48
 *
 */
@Service("applyService")
public class ApplyServiceImpl implements IApplyService {


	/** 日志对象 */
	private static Logger logger = Logger.getLogger(ApplyServiceImpl.class);
	
	@Autowired @Qualifier("apiService")
	private IAPIService apiService;
	
	@Autowired @Qualifier("applyDao")
	private IApplyDao applyDao;
	@Autowired
	private IApiOauthService oauthService;
	
	@Override
	public Result<String> saveApply(ApplyEntity entity) {
		Result<String> result = new Result<>();
		result.setStatus(Status.SUCCESS);
		
		
		if(null == entity.getUser()){
			result.setErrorMessage("请登录后操作!");
			return result;
		}
		
		
		//构建查询对象
		Criteria c = new Criteria();
		
		if(StringUtils.isNotBlank(entity.getUser().getId())){
			c.and("userId").is(entity.getUser().getId());
		}
		
		if(StringUtils.isNotBlank(entity.getApplyName())){
			c.and("applyName").is(entity.getApplyName());
		}
		
		Query query = new Query(c).with(new Sort(Direction.DESC,"createDate"));
		
		
		List<ApplyEntity> findPageByList = applyDao.findListByEntity(query);
		
		
		if(StringUtils.isBlank(entity.getApplyName())){
			result.setErrorMessage("应用名称不能为空!");
			return result;
		}
		
		//新增时
		if(StringUtils.isBlank(entity.getId())){
			if(null != findPageByList && findPageByList.size() > 0){
				result.setErrorMessage("应用名称不可重复!");
				return result;
			}
		}else{
			//修改时 判断是否数据库中重复 排除修改对象本身
			for (ApplyEntity applyEntity : findPageByList) {
				if(!applyEntity.getId().equals(entity.getId())){
					result.setErrorMessage("应用名称不可重复!");
					return result;
				}
			}
		}

		
		try {
			
			//新增
			if(StringUtils.isBlank(entity.getId())){
				entity.setUserId(entity.getUser().getId());
				entity.setUserName(entity.getUser().getUsername());
				entity.setCreateDate(new Date());

				logger.info("insert apply begin : " + JSON.toJSONString(entity));
				applyDao.saveOrUpdate(entity);
				logger.info("save end");
				
			}else{
			//修改
				ApplyEntity applyById = applyDao.getApplyById(entity.getId());
				
				if(null == applyById){
					result.setErrorMessage("请求的应用信息不存在!");
					return result;
				}else{
					
					if(StringUtils.isNotBlank(entity.getApplyName())){
						applyById.setApplyName(entity.getApplyName());
					}
					
					if(StringUtils.isNotBlank(entity.getApplyDesc())){
						applyById.setApplyDesc(entity.getApplyDesc());
					}
				}
				
				logger.info("update apply begin : " + JSON.toJSONString(applyById));
				applyDao.saveOrUpdate(applyById);
				logger.info("save end");
				
			}
			
		} catch (Exception e) {
			logger.error("saveApply system error,e:" + e.toString());
			if(StringUtils.isBlank(entity.getId())){
				result.setErrorMessage("新增应用时出现错误!");
			}else{
				result.setErrorMessage("修改应用时出现错误!");
			}
			
		}
		
		return result;
	}


	@Override
	public Result<String> deleteApplyByid(String id) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<>();
		try {
			ApplyEntity apply = applyDao.getApplyById(id);
			
			if(null == apply){
				result.setErrorMessage("请求删除的应用不存在!");
				return result;
			} else if(apply.getAuthIds() != null && apply.getAuthIds().size() > 0){
				result.setErrorMessage("应用下存在api,删除失败!");
				return result;
			} else {
				logger.info("delete apply begin applyId:" + id);
				applyDao.delete(id);
				logger.info("delete apply end");
				result.setSuccessMessage("删除成功!");
			} 
		} catch (Exception e) {
			logger.error("saveApply system error,e:" + e.toString());
			result.setErrorMessage("删除应用时出现错误!");
		}
		return result;
	}

	@Override
	public Result<Page<ApplyEntity>> findApplyList(ApplyEntity entity, int currentPage, int pageSize) {
		Result<Page<ApplyEntity>> result = new Result<>();
		try {
			Page<ApplyEntity> findPageByEntity = applyDao.findPageByEntity(generalApplyQuery(entity), currentPage, pageSize);
			result.setSuccessData(findPageByEntity);
		} catch (Exception e) {
			result.setErrorMessage("获取应用列表时出现错误!");
		}
		return result;
	}



	@Override
	public Result<List<ApplyEntity>> findApplyList(ApplyEntity entity) {
		return null;
	}



	@Override
	public Result<ApplyEntity> getApplyById(String id,int pageSize,int currentPage) {
		Result<ApplyEntity> result = new Result<>(); 
		
		try {
			ApplyEntity apply = applyDao.getApplyById(id);
			
			if(null == apply){
				result.setErrorMessage("该应用不存在!");
			} else if(null == apply.getAuthIds()){
				result.setErrorMessage("该应用下暂无api信息!");
			} else {
				List<String> authIds = apply.getAuthIds();
				if(null != authIds){
					int begin = (currentPage - 1) * pageSize;
					
					int end = pageSize * currentPage;
					
					if(authIds.size() < end){
						end = authIds.size();
					}
					
					authIds = authIds.subList(begin, end);
					
					List<ApiAuditEntity> apiAuditList = oauthService.getApiAuditEntity(authIds);
					
					apply.setAuditList(apiAuditList);
					result.setSuccessData(apply);
				}
			}
			
		} catch (Exception e) {
			logger.error("saveApply system error,e:" + e.toString());
			result.setErrorMessage("系统错误!");
		} 
		
		return result;
	}
	
	/***
	 * 根据实体对象构建查询条件
	 * @param entity 实体对象
	 * @author lida
	 * @return
	 */
	private Criteria generalApplyCriteria(ApplyEntity entity){
		//构建查询对象
		Criteria c = new Criteria();
		
		if(StringUtils.isNotBlank(entity.getId())){
			c.and("id").is(entity.getId());
		}
		
		if(StringUtils.isNotBlank(entity.getUserId())){
			c.and("userId").is(entity.getUserId());
		}
		
		if(StringUtils.isNotBlank(entity.getApplyName())){
			c.and("applyName").regex(entity.getApplyName());
		}
		
		return c;
	}

	private Query generalApplyQuery(ApplyEntity apply){
		Query query = new Query(generalApplyCriteria(apply)).with(new Sort(Direction.DESC,"createDate"));
		return query;
	}


	@Override
	public ApplyEntity findById(String applyId) {
		
		return applyDao.findById(applyId);
	}



}
