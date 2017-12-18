package cn.ce.platform_service.apis.service;

import javax.servlet.http.HttpServletResponse;

import cn.ce.platform_service.apis.entity.ApiExportParamEntity;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.users.entity.User;

/**
* @Description : 文件导入导出service
* @Author : makangwei
* @Date : 2017年12月14日
*/
public interface IApiTransportService {

	String exportApis(String recordId, HttpServletResponse response);

	Result<?> generalExportList(ApiExportParamEntity exportParam, User user);

	Result<?> importApis(String upStr, User user);

	Result<?> getExportRecords(Integer checkCurrentPage, Integer checkPageSize);

	Result<?> getExportRecordById(String recordId);

	Result<?> getImportRecords(Integer checkCurrentPage, Integer checkPageSize);

	Result<?> getImportRecordById(String recordId);
}
