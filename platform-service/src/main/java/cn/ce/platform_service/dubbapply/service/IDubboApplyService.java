package cn.ce.platform_service.dubbapply.service;

import cn.ce.platform_service.common.Result;

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
	public Result<?> findAppsByUnit(String unit, String appName, Integer currentPage, Integer pageSize);
	
//    /**
//     * 解析jar
//     * @param path
//     * @param dependcyFileName
//     * @param mainJarName
//     * @return
//     */
//	public boolean saveDubboApplyService(String path,String[] dependcyFileName,String mainJarName);

}
