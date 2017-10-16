package cn.ce.platform_service.openApply.service.impl;

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
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.core.bean.ConditionEnum;
import cn.ce.platform_service.core.bean.MongoDBWhereEntity;
import cn.ce.platform_service.openApply.dao.INewOpenApplyDao;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;
import cn.ce.platform_service.openApply.service.IConsoleOpenApplyService;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.util.SplitUtil;

/***
 * 
 * 
 * @ClassName:  ConsoleOpenApplyServiceImpl   
 * @Description:开放应用控制service实现类   
 * @author: lida 
 * @date:   2017年10月14日 下午2:48:59   
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 *
 */
@Service(value="colsoleOpenApplyService")
public class ConsoleOpenApplyServiceImpl implements IConsoleOpenApplyService{

	private static Logger _LOGGER = LoggerFactory.getLogger(ConsoleOpenApplyServiceImpl.class); 
	
	@Resource
	private INewOpenApplyDao newOpenApplyDao;
	
	@Override
	public Result<?> addApply(HttpSession session,OpenApplyEntity apply) {
		
		Result<String> result = new Result<String>();
		
		try{

			Map<String, MongoDBWhereEntity> whereEntity = new HashMap<>();
			
			List<OpenApplyEntity> tempEntityList;
			
			if(StringUtils.isNotBlank(apply.getApplyName())){
				whereEntity.put(MongoFiledConstants.OPEN_APPLY_APPLYNAME, 
						new MongoDBWhereEntity(apply.getApplyName(), ConditionEnum.EQ));
			}
			
			tempEntityList = newOpenApplyDao.findOpenApplyByEntity(whereEntity);
			
			if(tempEntityList != null && tempEntityList.size() > 0){
				result.setErrorMessage("应用名称已存在!",ErrorCodeNo.SYS009);
				return result;
			}
			
			whereEntity.clear();
			
			if(StringUtils.isNotBlank(apply.getApplyKey())){
				whereEntity.put(MongoFiledConstants.OPEN_APPLY_APPLYKEY, 
						new MongoDBWhereEntity(apply.getApplyKey(), ConditionEnum.EQ));
			}
			
			tempEntityList = newOpenApplyDao.findOpenApplyByEntity(whereEntity);

			if(tempEntityList != null && tempEntityList.size() > 0){
				result.setErrorMessage("应用key已存在!",ErrorCodeNo.SYS009);
				return result;
			}
			
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
			result.setSuccessMessage("保存成功!");
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
				
				Map<String, MongoDBWhereEntity> whereEntity = new HashMap<>();
				
				List<OpenApplyEntity> entityList;
				
				
				if(StringUtils.isNotBlank(openApply.getApplyKey())){
					whereEntity.put(MongoFiledConstants.OPEN_APPLY_APPLYKEY, 
							new MongoDBWhereEntity(openApply.getApplyKey(), ConditionEnum.EQ));
					whereEntity.put(MongoFiledConstants.OPEN_APPLY_ID, 
							new MongoDBWhereEntity(openApply.getId(), ConditionEnum.NEQ));					
				}
				
				entityList = newOpenApplyDao.findOpenApplyByEntity(whereEntity);
				
				if(entityList != null && entityList.size() > 0){
					result.setErrorMessage("应用key已存在!",ErrorCodeNo.SYS009);
					return result;
				}
				
				whereEntity.clear();
				
				if(StringUtils.isNotBlank(openApply.getApplyName())){
					whereEntity.put(MongoFiledConstants.OPEN_APPLY_APPLYNAME, 
							new MongoDBWhereEntity(openApply.getApplyName(), ConditionEnum.EQ));
					whereEntity.put(MongoFiledConstants.OPEN_APPLY_ID, 
							new MongoDBWhereEntity(openApply.getId(), ConditionEnum.NEQ));					
				}				
				
				entityList = newOpenApplyDao.findOpenApplyByEntity(whereEntity);
				
				if(entityList != null && entityList.size() > 0){
					result.setErrorMessage("应用名称已存在!",ErrorCodeNo.SYS009);
					return result;
				}
				
				newOpenApplyDao.save(openApply);
				result.setSuccessMessage("保存成功!");
				
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
		
		try{
			List<OpenApplyEntity> findOpenApplyByEntity = newOpenApplyDao.findOpenApplyByEntity(getSearchWhereMap(entity));
			result.setSuccessData(findOpenApplyByEntity);
		} catch (Exception e) {
			_LOGGER.error("查询应用时出现错误,e:" + e.toString());
			result.setErrorMessage("查询失败!");
		}
		
		//TODO
		return result;
	}
	
	public Result<Page<OpenApplyEntity>> applyList(OpenApplyEntity entity, Page<OpenApplyEntity> page) {
		
		Result<Page<OpenApplyEntity>> result = new Result<>();
		
		try {
			Page<OpenApplyEntity> findOpenApplyByEntity = newOpenApplyDao.findOpenApplyByEntity(getSearchWhereMap(entity), page);
			result.setSuccessData(findOpenApplyByEntity);
		} catch (Exception e) {
			_LOGGER.error("查询应用时出现错误,e:" + e.toString());
			result.setErrorMessage("查询失败!");
		}
		
		return result;
	}	


	@Override
	public Result<?> submitVerify(String id,Integer checkState) {
		
		Result<Page<OpenApplyEntity>> result = new Result<>();
		
		List<String> asList = SplitUtil.splitStringWithComma(id);
		
		try {
			newOpenApplyDao.batchSaveApply(asList, checkState);
			result.setSuccessMessage("保存成功!");
		} catch (Exception e) {
			_LOGGER.error("审核应用时出现错误,e:" + e.toString());
			result.setErrorMessage("保存失败!");
		}
		return result;
	}
	
	@Override
	public Result<?> getApplyById(String id) {
		Result<OpenApplyEntity> result = new Result<>();
		OpenApplyEntity findOpenApplyById = newOpenApplyDao.findOpenApplyById(id);
		if(findOpenApplyById == null){
			result.setErrorMessage("开放应用不存在!");
			result.setErrorCode(ErrorCodeNo.SYS015);
			return result;
		}
		result.setSuccessData(findOpenApplyById);
		// TODO Auto-generated method stub
		return result;
	}
	
	protected Map<String,MongoDBWhereEntity> getSearchWhereMap(OpenApplyEntity entity){
		
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
		return whereEntity;
	}
	
}
