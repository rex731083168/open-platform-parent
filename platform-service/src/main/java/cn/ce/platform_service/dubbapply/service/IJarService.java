package cn.ce.platform_service.dubbapply.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.users.entity.User;

/**
* @Description : jiar包上传下载service
* @Author : makangwei
* @Date : 2018年4月19日
*/
public interface IJarService {

	public Result<?> uploadMainJar(MultipartFile mainJar, User user);

	public Result<?> updateMainJar(String mainJarId, MultipartFile mainJar);

	public Result<?> deletedMainJar(String mainJarId);

	public Result<?> uploadDepJar(String mainJarId, MultipartFile[] depJar, User user);
	
	public Result<?> updateSingleDepJar(String depJarId, MultipartFile file);

	public Result<?> deleteSingleDepJar(String depJarId);

	public Result<?> deleteDepJars(List<String> depJarIds);
}

