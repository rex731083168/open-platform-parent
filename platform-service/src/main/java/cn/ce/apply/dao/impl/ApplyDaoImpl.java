package cn.ce.apply.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import cn.ce.apply.dao.IApplyDao;
import cn.ce.apply.entity.ApplyEntity;
import cn.ce.core.AbstractBaseMongoDao;
import cn.ce.page.Page;

/***
 * 应用数据层实现
 * @author lida
 * @date 2017年8月23日14:29:53
 *
 */
@Repository("applyDao")
public class ApplyDaoImpl extends AbstractBaseMongoDao<ApplyEntity> implements IApplyDao {

	@Override
	public void saveOrUpdate(ApplyEntity entity) {
		// TODO Auto-generated method stub
		if(StringUtils.isNotBlank(entity.getId())){
			super.updateById(entity.getId(), entity);
		}else{
			super.add(entity);
		}
	}

	@Override
	public void delete(String id) {
		super.delById(id, ApplyEntity.class);
	}

	@Override
	public Page<ApplyEntity> findPageByEntity(Query query,int currentPage,int pageSize) {
		return super.findAsPage(query, currentPage, pageSize, ApplyEntity.class);
	}

	@Override
	public List<ApplyEntity> findListByEntity(Query query) {
		return super.find(query, ApplyEntity.class);
	}

	@Override
	public void init() {
		
	}

	@Override
	public ApplyEntity getApplyById(String id) {
		return super.findById(id, ApplyEntity.class);
	}

	@Override
	public ApplyEntity findById(String applyId) {
		return super.findById(applyId, ApplyEntity.class);
	}

}
