package cn.ce.platform_manage.diyApply;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.diyApply.service.IManageDiyApplyService;
import io.swagger.annotations.Api;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月16日
*/
@RestController
@RequestMapping("/diyApply")
@Api("定制应用管理")
public class DiyApplyController {

	private static Logger _LOGGER = LoggerFactory.getLogger(DiyApplyController.class);
	
	@Resource
	IManageDiyApplyService manageDiyApplyService;
	
	
}
