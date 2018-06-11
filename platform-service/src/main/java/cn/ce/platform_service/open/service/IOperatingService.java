package cn.ce.platform_service.open.service;

import cn.ce.platform_service.common.Result;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年5月28日
*/
public interface IOperatingService {

	Result<?> api(Long startTimeStamp, Long endTimeStamp, Integer order, String apiId, Integer currentPage,
			Integer pageSize);

	Result<?> openApply(Long startTimeStamp, Long endTimeStamp, Integer order, String apiId, String openApplyId,
			Integer currentPage, Integer pageSize);

	Result<?> diyApply(Long startTimeStamp, Long endTimeStamp, Integer order, String apiId, String openApplyId,
			String diyApplyId, Integer currentPage, Integer pageSize);

}

