package cn.ce.platform_service.openApply.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.openApply.entity.OpenApplyEntity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月11日
*/
public interface IConsoleOpenApplyService {
	
	
	public Result<?> addApply(HttpSession session, OpenApplyEntity apply);
	
	public Result<?> modifyApply(OpenApplyEntity app);
	
	public Result<?> deleteApplyById(String appId);

	public Result<?> applyList(HttpServletRequest request, HttpServletResponse response);

	public Result<?> groupList(String userId, int currentPage, int pageSize);

	public Result<?> submitVerify(String id);
}
