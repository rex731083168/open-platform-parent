package cn.ce.platform_service.devapps.dao;

import java.util.List;
import org.springframework.data.mongodb.core.query.Query;
import cn.ce.platform_service.devapps.entity.DevAppsEntity;
import cn.ce.platform_service.page.Page;

/**
 *
 * @Title: DevAppsManagerDao.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月11日 time下午1:42:08
 *
 **/
public interface DevAppsDao {

	void saveOrUpdate(DevAppsEntity dae);

	void deleteDevApp(String id);

	Page<DevAppsEntity> findPageByEntity(Query query, Page<DevAppsEntity> page);

	DevAppsEntity findById(String id);
	
	List<DevAppsEntity> findListByEntity(Query query);

}
