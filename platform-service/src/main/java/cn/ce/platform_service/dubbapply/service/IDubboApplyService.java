package cn.ce.platform_service.dubbapply.service;

import org.springframework.web.multipart.MultipartFile;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.dubbapply.entity.Interfaceapplyentity.DubboApps;

public interface IDubboApplyService {
	
	/**
	 * 获取物理应用列表
	 * 
	 * @param unit
	 * @param pageSize 
	 * @param currentPage 
	 * @param appName 
	 * @return
	 */
	public Result<DubboApps> findAppsByUnit(String unit, String appName, Integer currentPage, Integer pageSize);
	
//    /**
//     * 解析jar
//     * @param path
//     * @param dependcyFileName
//     * @param mainJarName
//     * @return
//     */
//	public boolean saveDubboApplyService(String path,String[] dependcyFileName,String mainJarName);

}
