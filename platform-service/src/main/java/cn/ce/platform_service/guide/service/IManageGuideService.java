package cn.ce.platform_service.guide.service;

import java.security.interfaces.RSAKey;
import java.util.List;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.guide.entity.GuideEntity;

/**
 *
 * @Title: IManageGuideService.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月12日 time下午7:18:34
 *
 **/
public interface IManageGuideService {

	public Result<Page<GuideEntity>> guideList(String guideName, String creatUserName,
			int currentPage, int pageSize);

	public Result<String> batchUpdate(List<String> ids);
	
	public Result<String> auditUpdate(String id ,int checkState,String checkMem);
	
	public Result<GuideEntity> getByid(String id);
}
