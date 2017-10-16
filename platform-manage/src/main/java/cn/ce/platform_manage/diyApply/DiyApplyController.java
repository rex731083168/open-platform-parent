package cn.ce.platform_manage.diyApply;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.diyApply.IManageDiyApplyService;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月16日
*/
@RestController
@RequestMapping("/diyApply")
public class DiyApplyController {

	private static Logger _LOGGER = LoggerFactory.getLogger(DiyApplyController.class);
	
	@Resource
	IManageDiyApplyService manageDiyApplyService;
	
	@RequestMapping(value = "/diyApplyList", method=RequestMethod.POST)
	public Result<?> diyApplyList(
			@RequestParam(required=false)String applyNameLike,
			@RequestParam(required=false)Integer checkState,
			@RequestParam(required=false)String userNameLike,
			@RequestParam(required=false)String productNameLike,
			@RequestParam(required=false, defaultValue = "1")Integer currentPage,
			@RequestParam(required=false, defaultValue = "10")Integer pageSize){
		
		_LOGGER.info("applyNameLike:"+applyNameLike);
		_LOGGER.info("checkState:"+checkState);
		_LOGGER.info("userNameLike:"+userNameLike);
		_LOGGER.info("productNameLike:"+productNameLike);
		
		return manageDiyApplyService.getApplyList();
	}
}
