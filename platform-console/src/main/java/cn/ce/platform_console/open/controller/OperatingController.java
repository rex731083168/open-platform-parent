package cn.ce.platform_console.open.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.open.service.IOperatingService;

/**
* @Description : api bi 热度统计api
* @Author : makangwei
* @Date : 2018年5月28日
*/
@RestController
@RequestMapping("/open/hot")
public class OperatingController {

	@Resource
	private IOperatingService operatingService;
	

	@RequestMapping(value="/api/{startTimeStamp}/{endTimeStamp}",method=RequestMethod.GET)
	public Result<?> api(
			@PathVariable Long startTimeStamp,
			@PathVariable Long endTimeStamp,
			@RequestParam(required=false)Integer order,
			@RequestParam(required=false)String apiId,
			@RequestParam(required=false)Integer currentPage,
			@RequestParam(required=false)Integer pageSize){
		
		return operatingService.api(startTimeStamp,endTimeStamp,order,apiId,currentPage,pageSize);
	}
	
	@RequestMapping(value="openApply/{startTimeStamp}/{endTimeStamp}", method=RequestMethod.GET)
	public Result<?> openApply(			
			@PathVariable Long startTimeStamp,
			@PathVariable Long endTimeStamp,
			@RequestParam(required=false)Integer order,
			@RequestParam(required=false)String apiId,
			@RequestParam(required=false)String openApplyId,
			@RequestParam(required=false)Integer currentPage,
			@RequestParam(required=false)Integer pageSize){
		if(StringUtils.isBlank(openApplyId)){
			apiId = null;
		}
		return operatingService.openApply(startTimeStamp,endTimeStamp,order,apiId,openApplyId,currentPage,pageSize);
	}
	
	@RequestMapping(value="diyApply/{startTimeStamp}/{endTimeStamp}", method=RequestMethod.GET)
	public Result<?> diyApply(
			@PathVariable Long startTimeStamp,
			@PathVariable Long endTimeStamp,
			@RequestParam(required=false)Integer order,
			@RequestParam(required=false)String apiId,
			@RequestParam(required=false)String openApplyId,
			@RequestParam(required=false)String diyApplyId,
			@RequestParam(required=false)Integer currentPage,
			@RequestParam(required=false)Integer pageSize){
		if(StringUtils.isBlank(diyApplyId)){
			openApplyId = null;
			apiId = null;
		}else if(StringUtils.isBlank(openApplyId)){
			apiId = null;
		}
		
		return operatingService.diyApply(startTimeStamp,endTimeStamp,order,apiId,openApplyId,
				diyApplyId,currentPage,pageSize);
	}

	
}

