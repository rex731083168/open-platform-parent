package cn.ce.platform_service.guide.service;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.guide.entity.GuideEntity;
import cn.ce.platform_service.guide.entity.QueryGuideEntity;
import cn.ce.platform_service.users.entity.User;

/**
 *
 * @Title: IConsoleGuideService.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月12日 time下午7:12:15
 *
 **/
public interface IConsoleGuideService {

	public Result<String> add(User user, GuideEntity g);

	public Result<String> update(GuideEntity g);
	
	public Result<String> delete(String id);
	
	public Result<GuideEntity> getByid(String id);

	public Result<Page<GuideEntity>> guideList(QueryGuideEntity entity);

	Result<String> submitVerify(String id);

//	public Result<String> migraGuide();


}
