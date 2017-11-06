package cn.ce.platform_service.guide.service;

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

	public Result<Page<GuideEntity>> guideList(String guideName, String creatUserName,String applyId,Integer checkState, int currentPage,
			int pageSize);

	public Result<GuideEntity> getByid(String id);

	Result<String> batchUpdate(List<String> ids, Integer state, String checkMem);
}