package cn.ce.platform_service.openApply.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.openApply.dao.INewOpenApplyDao;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;
import cn.ce.platform_service.openApply.service.IConsoleOpenApplyService;
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
	
	@Override
	public Result<?> addApply(HttpSession session,OpenApplyEntity apply) {
		
		Result<String> result = new Result<String>();
		
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
		return result;
	}

	@Override
	public Result<?> modifyApply(OpenApplyEntity openApply) {
		
		Result<String> result = new Result<String>();
		
		OpenApplyEntity apply = newOpenApplyDao.findOpenApplyById(openApply.getId());
		
		if (null == apply) {
			result.setErrorMessage("当前开放应用不存在", ErrorCodeNo.SYS006);
		} else {
			// TODO 这里可否去掉neqId
			openApply.setNeqId(openApply.getId());// 查询非当前修改数据进行判断  
			List<OpenApplyEntity> entityList = newOpenApplyDao.findOpenApplyByNameOrKey(openApply.getApplyName(), openApply.getApplyKey());
			if (null != entityList && entityList.size() == 0) {
				newOpenApplyDao.save(openApply);
				result.setSuccessMessage("");
			} else {
				result.setErrorMessage("");
			}
		}
		
		return null;
	}

	@Override
	public Result<?> deleteApplyById(String appId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<?> applyList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<?> groupList(String userId, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result<?> submitVerify(String id) {
		// TODO Auto-generated method stub
		return null;
	}
}
