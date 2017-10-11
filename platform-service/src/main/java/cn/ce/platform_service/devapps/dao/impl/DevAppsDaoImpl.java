package cn.ce.platform_service.devapps.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.query.Query;
import cn.ce.platform_service.core.mongo.BaseMongoDaoImpl;
import cn.ce.platform_service.devapps.dao.DevAppsDao;
import cn.ce.platform_service.devapps.entity.DevAppsEntity;
import cn.ce.platform_service.page.Page;

/**
 *
 * @Title: DevAppsManagerDaoImpl.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月11日 time下午1:43:34
 *
 **/
public class DevAppsDaoImpl extends BaseMongoDaoImpl<DevAppsEntity> implements DevAppsDao {

	
	@Override
	public void saveOrUpdate(DevAppsEntity dae) {
		// TODO Auto-generated method stub
		if (StringUtils.isNotBlank(dae.getId())) {
			super.update(dae);
		} else {
			super.save(dae);
		}
	}

	@Override
	public void deleteDevApp(String id) {
		// TODO Auto-generated method stub
		super.removeById(id);
	}

	@Override
	public Page<DevAppsEntity> findPageByEntity(Query query, Page<DevAppsEntity> page) {
		// TODO Auto-generated method stub
		return super.findPage(page, query);
	}

	@Override
	public List<DevAppsEntity> findListByEntity(Query query) {
		// TODO Auto-generated method stub
		return super.find(query);
	}

}
