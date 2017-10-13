package cn.ce.platform_console.diyApply.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.diyApply.entity.applyProduct.ApplyProduct;
import cn.ce.platform_service.diyApply.service.IConsoleDiyApplyService;

/**
 *
 * @Title: DiyApplyProductController.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月13日 time上午11:02:16
 *
 **/
@RestController
@RequestMapping("/diyapplyProduct")
public class DiyApplyProductController {

	@Resource
	private IConsoleDiyApplyService consoleDiyApplyService;

	@RequestMapping(value = "/getApplyProduct", method = RequestMethod.POST)
	public Result<ApplyProduct> getApplyProduct(@RequestParam(value = "key", required = true) String key) {
		return consoleDiyApplyService.getApplyProductByKey(key);
	}

}
