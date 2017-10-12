package cn.ce.platform_service.diyApply.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import cn.ce.platform_service.core.mongo.BaseMongoDaoImpl;
import cn.ce.platform_service.diyApply.dao.IDiyApplyDao;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.page.Page;


/***
 * 
 * 
 * @ClassName:  ApplyDaoImpl   
 * @Description:应用数据层实现 
 * @author: lida 
 * @date:   2017年8月23日14:29:53   
 * @Copyright: 2017 中企动力科技股份有限公司 © 1999-2017 300.cn All Rights Reserved
 *
 */
@Repository("diyApplyDao")
public class DiyApplyDaoImpl extends BaseMongoDaoImpl<DiyApplyEntity> implements IDiyApplyDao {

	@Override
	public void saveOrUpdate(DiyApplyEntity entity) {
		// TODO Auto-generated method stub
		if(StringUtils.isNotBlank(entity.getId())){
			super.update(entity);
		}else{
			super.save(entity);
		}
	}

	@Override
	public void delete(String id) {
		super.removeById(id);
	}

	@Override
	public Page<DiyApplyEntity> findPageByEntity(Query query,Page<DiyApplyEntity> page) {
		return super.findPage(page, query);
	}

	@Override
	public List<DiyApplyEntity> findListByEntity(Query query) {
		return super.find(query);
	}


	@Override
	public DiyApplyEntity findById(String id) {
		return super.findById(id);
	}

}
