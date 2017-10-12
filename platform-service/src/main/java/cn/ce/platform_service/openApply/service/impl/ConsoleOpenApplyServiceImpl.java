package cn.ce.platform_service.openApply.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.MongoFiledConstants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.core.bean.ConditionEnum;
import cn.ce.platform_service.core.bean.MongoDBWhereEntity;
import cn.ce.platform_service.openApply.dao.INewOpenApplyDao;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;
import cn.ce.platform_service.openApply.service.IConsoleOpenApplyService;
import cn.ce.platform_service.page.Page;
import cn.ce.platform_service.users.entity.User;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月11日
*/
@Service(value="colsoleOpenApplyService")
public class ConsoleOpenApplyServiceImpl implements IConsoleOpenApplyService{

	private static Logger _LOGGER = LoggerFactory.getLogger(ConsoleOpenApplyServiceImpl.class); 
	
	@Resource
	private INewOpenApplyDao newOpenApplyDao;
	
	@Resource
	
	@Override
	public Result<?> addApply(HttpSession session,OpenApplyEntity apply) {
		
		Result<String> result = new Result<String>();
		
		try{
			//判断当前服务名称和key是否已经被占用
			List<OpenApplyEntity> tempEntityList = newOpenApplyDao.findOpenApplyByNameOrKey(apply.getApplyName(),apply.getApplyKey());
			if (tempEntityList != null && tempEntityList.size() ==0) { //未被占用
				User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
				if (user.getUserType() == AuditConstants.USER_ADMINISTRATOR) {
					apply.setCheckState(AuditConstants.OPEN_APPLY_CHECKED_SUCCESS); // 后台系统添加服务分类默认审核通过
				} else {
					apply.setCheckState(AuditConstants.OPEN_APPLY_UNCHECKED); //提供者添加服务分类需要提交审核
				}
				apply.setCreateDate(new Date());
				apply.setUserId(user.getId());
				apply.setUserName(user.getUserName());
				apply.setEnterpriseName(user.getEnterpriseName());
	
				// appKey不能以/开头和结尾
				if (apply.getApplyKey().startsWith("/") || apply.getApplyKey().endsWith("/")) {
					result.setErrorMessage("appKey不能以/开头和/结尾");
					return result;
				}
				newOpenApplyDao.save(apply);
				result.setSuccessMessage("");
			} else{
				result.setErrorMessage("服务分类标识已存在!",ErrorCodeNo.SYS009);
			}
		} catch (Exception e) {
			_LOGGER.error("添加应用时出现错误,e:" + e.toString());
			result.setErrorMessage("保存失败!");
		}	
		
		return result;
	}

	@Override
	public Result<?> modifyApply(OpenApplyEntity openApply) {
		
		Result<String> result = new Result<String>();
		
		try {
			
			OpenApplyEntity apply = newOpenApplyDao.findOpenApplyById(openApply.getId());
			
			if (null == apply) {
				result.setErrorMessage("当前开放应用不存在", ErrorCodeNo.SYS006);
			} else {
				
				List<OpenApplyEntity> entityList = newOpenApplyDao.findOpenApplyByNeqId(openApply);
				
				if (null != entityList && entityList.size() == 0) {
					newOpenApplyDao.save(openApply);
					result.setSuccessMessage("保存成功!");
				} else {
					result.setErrorMessage("appKey或appName不能重复!");
				}
			}
		} catch (Exception e) {
			_LOGGER.error("修改应用时出现错误,e:" + e.toString());
			result.setErrorMessage("保存失败!");
		}
		
		return result;
	}

	@Override
	public Result<?> deleteApplyById(String appId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<List<OpenApplyEntity>> applyList(OpenApplyEntity entity) {
		Result<List<OpenApplyEntity>> result = new Result<>();
		
		Map<String, MongoDBWhereEntity> whereEntity = new HashMap<>();
		
		if(StringUtils.isNotBlank(entity.getApplyName())){
			whereEntity.put(MongoFiledConstants.OPEN_APPLY_APPLYNAME, 
					new MongoDBWhereEntity(entity.getApplyName(), ConditionEnum.LIKE));
		}
		
		if(StringUtils.isNotBlank(entity.getUserName())){
			whereEntity.put(MongoFiledConstants.OPEN_APPLY_USERNAME, 
					new MongoDBWhereEntity(entity.getUserName(), ConditionEnum.LIKE));
		}
		
		if(null != entity.getCheckState()){
			whereEntity.put(MongoFiledConstants.OPEN_APPLY_CHECKSTATE, 
					new MongoDBWhereEntity(entity.getCheckState(), ConditionEnum.EQ));
		}		
		
		try{
			List<OpenApplyEntity> findOpenApplyByEntity = newOpenApplyDao.findOpenApplyByEntity(whereEntity);
			result.setData(findOpenApplyByEntity);
			result.setStatus(Status.SUCCESS);
		} catch (Exception e) {
			_LOGGER.error("查询应用时出现错误,e:" + e.toString());
			result.setErrorMessage("查询失败!");
		}
		
		//TODO
		return result;
	}
	
	public Result<Page<OpenApplyEntity>> applyList(OpenApplyEntity entity, Page<OpenApplyEntity> page) {
		Result<Page<OpenApplyEntity>> result = new Result<>();
		Map<String, MongoDBWhereEntity> whereEntity = new HashMap<>();
		
		if(StringUtils.isNotBlank(entity.getApplyName())){
			whereEntity.put(MongoFiledConstants.OPEN_APPLY_APPLYNAME, 
					new MongoDBWhereEntity(entity.getApplyName(), ConditionEnum.LIKE));
		}
		
		if(StringUtils.isNotBlank(entity.getUserName())){
			whereEntity.put(MongoFiledConstants.OPEN_APPLY_USERNAME, 
					new MongoDBWhereEntity(entity.getUserName(), ConditionEnum.LIKE));
		}
		
		if(null != entity.getCheckState()){
			whereEntity.put(MongoFiledConstants.OPEN_APPLY_CHECKSTATE, 
					new MongoDBWhereEntity(entity.getCheckState(), ConditionEnum.EQ));
		}
		
		try {
			Page<OpenApplyEntity> findOpenApplyByEntity = newOpenApplyDao.findOpenApplyByEntity(whereEntity, page);
			result.setData(findOpenApplyByEntity);
			result.setStatus(Status.SUCCESS);
		} catch (Exception e) {
			_LOGGER.error("查询应用时出现错误,e:" + e.toString());
			result.setErrorMessage("查询失败!");
		}
		
		return result;
	}	


	@Override
	public Result<?> submitVerify(String id,Integer checkState) {
		Result<Page<OpenApplyEntity>> result = new Result<>();
		List<String> asList = Arrays.asList(id.split(","));
		try {
			newOpenApplyDao.batchSaveApply(asList, checkState);
			result.setSuccessMessage("保存成功!");
		} catch (Exception e) {
			_LOGGER.error("审核应用时出现错误,e:" + e.toString());
			result.setErrorMessage("保存失败!");
		}
		return result;
	}
}
