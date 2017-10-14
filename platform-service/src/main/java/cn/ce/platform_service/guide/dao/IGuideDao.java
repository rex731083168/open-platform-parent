package cn.ce.platform_service.guide.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import cn.ce.platform_service.guide.entity.GuideEntity;
import cn.ce.platform_service.common.page.Page;

/**
 *
 * @Title: IGuideDao.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月12日 time下午5:49:31
 *
 **/
public interface IGuideDao {

	public void saveOrUpdateGuide(GuideEntity g);

	public Page<GuideEntity> listByPage(Page<GuideEntity> page, Query query);
	
	public List<GuideEntity> list(Query query);
	
	public GuideEntity getById(String id);

	public int bachUpdateGuide(List<String> ids);
	
	public void deleteByid(String id);
	
}
