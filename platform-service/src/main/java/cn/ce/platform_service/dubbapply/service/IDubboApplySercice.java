package cn.ce.platform_service.dubbapply.service;

import java.util.List;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.dubbapply.entity.DubboApplyEntity;

public interface IDubboApplySercice {
	
    /**
     * 解析jar
     * @param path
     * @param dependcyFileName
     * @param mainJarName
     * @return
     */
	public boolean saveDubboApplySercice(String path,String[] dependcyFileName,String mainJarName);
}
