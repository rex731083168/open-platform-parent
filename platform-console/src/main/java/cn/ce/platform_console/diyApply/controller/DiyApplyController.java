package cn.ce.platform_console.diyApply.controller;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.QueryDiyApplyEntity;
import cn.ce.platform_service.diyApply.service.IConsoleDiyApplyService;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.util.CoverBeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/***
 * 
 * 
 * @ClassName: DiyApplyController
 * @Description:定制应用控制类
 * @author: lida
 * @date: 2017年10月14日 下午3:41:37
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 *
 */
@RestController
@RequestMapping("/diyApply")
@Api("定制应用控制类")
public class DiyApplyController {

	private static Logger _LOGGER = LoggerFactory.getLogger(DiyApplyController.class);

	@Resource
	private IConsoleDiyApplyService consoleDiyApplyService;

	@RequestMapping(value = "/findApplyList", method = RequestMethod.POST)
	@ApiOperation(value = "根据条件查询应用列表_TODO", httpMethod = "POST", response = Result.class, notes = "根据条件查询应用列表")
//	public Result<?> findApplyList(HttpSession session,
//			@RequestBody QueryDiyApplyEntity queryApply) {
	public Result<?> findApplyList(HttpSession session, @RequestBody DiyApplyEntity apply,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage) {
		
		QueryDiyApplyEntity queryApply = new QueryDiyApplyEntity();
		try {
			CoverBeanUtils.copyProperties(queryApply, apply);
		} catch (IllegalAccessException | InvocationTargetException e1) {
			e1.printStackTrace();
			return new Result<String>("类转化异常",ErrorCodeNo.SYS004,null,Status.FAILED);
		}
		queryApply.setApplyId(apply.getId());
		queryApply.setCurrentPage(currentPage);
		queryApply.setPageSize(pageSize);
		
		//只获取当前登录的用户数据,如果获取数据失败就会报异常
		// TODO 20180205 mkw 这里的 try catch 可以去掉 因为前置的登录拦截器已经判断是否session中有用户数据了
		User user = null;
		try{
			user = (User)session.getAttribute(Constants.SES_LOGIN_USER);
		}catch(Exception e){
			_LOGGER.error("session已经过期");
			return new Result<String>("session已过期", ErrorCodeNo.SYS019, null, Status.FAILED);
		}
		queryApply.setUserId(user.getId());
		
		return consoleDiyApplyService.findApplyList(queryApply);
	}

	@RequestMapping(value = "/deleteApplyByid", method = RequestMethod.DELETE)
	@ApiOperation("根据标识删除应用_TODO")
	public Result<?> deleteApplyByid(HttpServletResponse response,
			@RequestParam(value = "id", required = true) String id) {

		return consoleDiyApplyService.deleteApplyByid(id);

	}

	@RequestMapping(value = "/getApplyById", method = RequestMethod.GET)
	@ApiOperation("根据应用标识查询应用")
	public Result<?> getApplyById(@RequestParam(required = true) String applyId) {

		return consoleDiyApplyService.findById(applyId);
	}

	/**
	 * 
	 * @Title: saveApply
	 * @Description: 做兼容处理，如果包含id支持update
	 * @author: makangwei
	 * @date:   2018年2月8日 下午7:29:09  
	 */
	@RequestMapping(value = "/saveApply", method = RequestMethod.POST)
	@ApiOperation("新增或修改应用")
	public Result<?> saveApply(HttpSession session, @RequestBody DiyApplyEntity apply) {

		if (StringUtils.isBlank(apply.getApplyName())) {
			return new Result<String>("应用名称不能为空!",ErrorCodeNo.SYS005,null,Status.FAILED);
		}

		User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);

		apply.setUser(user);

		return consoleDiyApplyService.saveApply(apply);

	}

	@RequestMapping(value = "/updateApply", method = RequestMethod.POST)
	@ApiOperation("修改应用，不能修改产品密钥")
	public Result<?> updateApply(HttpSession session, @RequestBody DiyApplyEntity apply) {

		return consoleDiyApplyService.updateApply(apply);
	}

//	@RequestMapping(value = "/batchCommit", method = RequestMethod.POST)
	@RequestMapping(value = "/batchUpdate", method = RequestMethod.POST)
	@ApiOperation("批量发布应用 更改checkState为1_TODO")
	public Result<String> batchUpdate(@RequestParam String ids) {
		
		return consoleDiyApplyService.batchUpdateCheckState(ids, AuditConstants.DIY_APPLY_CHECKED_COMMITED, null);
	}

}
