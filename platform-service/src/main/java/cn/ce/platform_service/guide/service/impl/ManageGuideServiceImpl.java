package cn.ce.platform_service.guide.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.guide.dao.IGuideDao;
import cn.ce.platform_service.guide.entity.GuideEntity;
import cn.ce.platform_service.guide.service.IManageGuideService;

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
	public Result<Page<GuideEntity>> guideList(String guideName, String creatUserName, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		Result<Page<GuideEntity>> result = new Result<Page<GuideEntity>>();
		Page<GuideEntity> page = new Page<GuideEntity>(currentPage, 0, pageSize);
		Criteria c = new Criteria();
		c.and("checkState").is(AuditConstants.GUIDE_COMMITED);
		if (StringUtils.isNotBlank(guideName)) {
			c.and("guideName").regex(guideName);
		}
		if (StringUtils.isNotBlank(creatUserName)) {
			c.and("creatUserName").regex(creatUserName);
		}
		Query query = new Query(c).with(new Sort(Direction.DESC, "creatTime"));
		result.setSuccessData(guideDaoImpl.listByPage(page, query));
		return result;
	}

	@Override
	public Result<String> batchUpdate(List<String> ids,Integer state) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
		try {
			String message = String.valueOf(guideDaoImpl.bachUpdateGuide(ids,state));
			_LOGGER.info("bachUpdate guide message " + message + " count");
			result.setSuccessMessage("审核成功:" + message + "条");
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			_LOGGER.info("bachUpdate guide message faile " + e + " ");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("审核失败");
			return result;
		}
	}

	@Override
	public Result<GuideEntity> getByid(String id) {
		// TODO Auto-generated method stub
		Result<GuideEntity> result = new Result<GuideEntity>();
		result.setData(guideDaoImpl.getById(id));
		return result;
	}
}
