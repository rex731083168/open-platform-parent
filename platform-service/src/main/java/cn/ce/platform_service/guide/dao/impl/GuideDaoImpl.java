package cn.ce.platform_service.guide.dao.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.core.BathUpdateOptions;
import cn.ce.platform_service.core.mongo.BaseMongoDaoImpl;
import cn.ce.platform_service.guide.dao.IGuideDao;
import cn.ce.platform_service.guide.entity.GuideEntity;

/**
 *
 * @Title: GuideDaoImpl.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月12日 time下午6:51:38
 *
 **/
@Repository(value = "guideDaoImpl")
public class GuideDaoImpl extends BaseMongoDaoImpl<GuideEntity> implements IGuideDao {

	@Override
	public void saveOrUpdateGuide(GuideEntity g) {
		// TODO Auto-generated method stub
		if (StringUtils.isNotBlank(g.getId())) {
			super.update(g);
		} else {
			super.save(g);
		}
	}

	@Override
	public Page<GuideEntity> listByPage(Page<GuideEntity> page, Query query) {
		// TODO Auto-generated method stub
		return super.findPage(page, query);
	}

	@Override
	public List<GuideEntity> list(Query query) {
		return super.find(query);
	}

	@Override
	public String bachUpdateGuide(List<String> ids, Integer state, String checkMem) {
		// TODO Auto-generated method stub
		DecimalFormat df = new DecimalFormat("0");
		List<BathUpdateOptions> list = new ArrayList<BathUpdateOptions>();
		for (int i = 0; i < ids.size(); i++) {
			list.add(new BathUpdateOptions(Query.query(Criteria.where("_id").is(new ObjectId(ids.get(i)))),
					Update.update("checkState", state), false, true));
			list.add(new BathUpdateOptions(Query.query(Criteria.where("_id").is(new ObjectId(ids.get(i)))),
					Update.update("checkMem", checkMem), false, true));

		}
		return df.format((float)(super.bathUpdate(super.mongoTemplate, GuideEntity.class, list))/list.size());
	}

	@Override
	public GuideEntity getById(String id) {
		// TODO Auto-generated method stub
		return super.findById(id);
	}

	@Override
	public void deleteByid(String id) {
		// TODO Auto-generated method stub
		super.removeById(id);
	}

}
