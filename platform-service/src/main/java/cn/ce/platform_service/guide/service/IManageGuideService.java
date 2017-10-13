package cn.ce.platform_service.guide.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.guide.entity.GuideEntity;
import cn.ce.platform_service.common.page.Page;

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

	public Result<String> batchUpdate(String ids);
	
	public Result<GuideEntity> getByid(String id);
}
