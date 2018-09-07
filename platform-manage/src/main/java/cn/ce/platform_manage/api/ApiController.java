package cn.ce.platform_manage.api;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.apis.entity.QueryApiEntity;
import cn.ce.platform_service.apis.service.IManageApiService;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.openApply.service.IManageOpenApplyService;
import cn.ce.platform_service.util.SplitUtil;
import io.swagger.annotations.ApiOperation;

/**
* @Description : 后台api管理
* @Author : makangwei
* @Date : 2017年8月18日
*/
@RestController
@RequestMapping("/api")
public class ApiController {
	

	private static Logger _LOGGER = LoggerFactory.getLogger(ApiController.class);

    @Resource
    private IManageApiService manageApiService;
    
    @Resource
    private IManageOpenApplyService manageOpenApplyService;
    
	/**
	 * @Description : 审核后推送网关是多版本+oauth，批量审核，多个api用逗号隔开
	 * @Author : makangwei
	 * @Date : 2017年8月21日
	 * @param apiIds
	 * @param checkState 审核状态 2：通过，3：拒绝
	 * @param checkMem  如果审核失败，此字段输入审核失败原因
	 * @return
	 */
	@RequestMapping(value = "/auditApi", method = RequestMethod.POST)
	public Result<?> auditApi(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam String apiIds,
			@RequestParam Integer checkState, 
			@RequestParam(required=false) String checkMem) { // checkMem如过审核不通过，将审核失败的原因通过该字段传入

		List<String> apiId = SplitUtil.splitStringWithComma(apiIds);
		_LOGGER.info("----------apiId:" + apiId);
		_LOGGER.info("----------checkState:" + checkState);
		_LOGGER.info("----------checkMem:" + checkMem);

		if (apiIds == null || apiIds.trim() == "") {
			Result<String> result = new Result<String>();
			result.setMessage("apiId不能为空");
			return result;
		}
		if (checkState == null || checkState > 3 || checkState < 0) {
			Result<String> result = new Result<String>();
			result.setMessage("审核状态不存在，请输入正确的审核状态");
			return result;
		}

		Result<?> result = manageApiService.auditApi(apiId, checkState, checkMem,true);
		_LOGGER.info("api:"+apiIds+"，批量审核成功");
		return result;
	}

	/**
	 * @Title: showApi
	 * @Description: 单个api查询
	 * @author: makangwei
	 * @date:   2017年10月16日 下午3:41:04
	 */
	@RequestMapping(value = "/showApi", method = RequestMethod.POST)
	@ApiOperation("显示api详情_TODO")
	public Result<?> showApi(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(required=true)String apiId) {

		_LOGGER.info("当前后台管理系统查询的apiId为："+apiId);
		
		return manageApiService.showApi(apiId);
	}

	/**
	 * 
	 * @Description: api列表查询
	 * @author: makangwei 
	 * @date:   2017年11月14日 下午2:15:32 
	 */
	@RequestMapping(value = "/apiList", method = RequestMethod.POST)
	@ApiOperation("api列表_TODO")
//	public Result<?> showAPIs(HttpServletRequest request, HttpServletResponse response, 
//			@RequestBody QueryApiEntity apiEntity,
//			@RequestParam(required=false,defaultValue="true")boolean enablePaged){
	public Result<?> showAPIs(HttpServletRequest request, HttpServletResponse response, 
			@RequestBody QueryApiEntity apiEntity,
			@RequestParam(required=false,defaultValue= "1") int currentPage, 
			@RequestParam(required=false,defaultValue= "10")int pageSize,
			@RequestParam(required=false,defaultValue="true")boolean enablePaged){

		apiEntity.setCurrentPage(currentPage);
		apiEntity.setPageSize(pageSize);
		if(true == enablePaged){
			return manageApiService.apiList(apiEntity);
		}else{
			return manageApiService.apiAllList(apiEntity);
		}
	}


}
