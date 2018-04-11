package cn.ce.platform_service.dubbapply.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import cn.ce.platform_service.common.Result;

public interface IDubboApplyService {
	
    /**
     * 解析jar
     * @param path
     * @param dependcyFileName
     * @param mainJarName
     * @return
     */
	public boolean saveDubboApplySercice(String path,String[] dependcyFileName,String mainJarName);

	public Result<?> uploadMainJar(MultipartFile mainJar);

	public Result<?> uploadDepJar(String mainJarId, MultipartFile[] depJar);
}
