package cn.ce.platform_service.guide.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.guide.dao.IGuideDao;
import cn.ce.platform_service.guide.entity.GuideEntity;
import cn.ce.platform_service.guide.service.IManageGuideService;
import cn.ce.platform_service.common.page.Page;

/**
 *
 * @Title: ManageGuideServiceImpl.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月12日 time下午7:51:16
 *
 **/
@Service("iManageGuideService")
public class ManageGuideServiceImpl implements IManageGuideService {
	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(ConsoleGuideServiceImpl.class);
	@Resource
	private IGuideDao guideDaoImpl;

	@Override
	public Page<GuideEntity> guideList(HttpSession session, String guideName, String creatUserName, int currentPage,
			int pageSize) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
		Page<GuideEntity> page = new Page<GuideEntity>(currentPage, 0, pageSize);
		Criteria c = new Criteria();
		if (StringUtils.isNotBlank(guideName)) {

			c.and("guideName").regex(guideName);
		}
		if (StringUtils.isNotBlank(creatUserName)) {

			c.and("creatUserName").regex(creatUserName);
		}
		Query query = new Query(c).with(new Sort(Direction.DESC, "creatTime"));

		return guideDaoImpl.list(page, query);
	}

	@Override
	public Result<String> batchUpdate(List<String> ids) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
		String message = String.valueOf(guideDaoImpl.bachUpdateGuide(ids));
		_LOGGER.info("bachUpdate message " + message + " count");
		result.setMessage(message);
		return result;
	}

	@Override
	public Result<GuideEntity> getByid(String id) {
		// TODO Auto-generated method stub
		Result<GuideEntity> result = new Result<GuideEntity>();
		result.setData(guideDaoImpl.getById(id));
		return result;
	}
}
