package cn.ce.platform_service.dubbapply.service;

import javax.servlet.http.HttpServletResponse;

import cn.ce.platform_service.common.Result;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年4月28日
*/
public interface IJarShowService {

	Result<?> downLoadJar(String mainJarId, HttpServletResponse response);

	Result<?> parseMainJar(String mainJarId);

	Result<?> getMainJarList(String originalFileName, Integer currentPage, Integer pageSize);

	Result<?> getDepJarList(String mainJarId, String originalFileName, Integer currentPage, Integer pageSize);

	Result<?> getMainJarParsedMethods(String mainJarId, String methodName, Integer currentPage, Integer pageSize);
	
}

