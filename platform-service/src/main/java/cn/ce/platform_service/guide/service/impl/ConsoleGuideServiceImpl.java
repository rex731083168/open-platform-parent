package cn.ce.platform_service.guide.service.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.guide.dao.IGuideDao;
import cn.ce.platform_service.guide.entity.GuideEntity;
import cn.ce.platform_service.guide.service.IConsoleGuideService;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.users.entity.User;

/**
 *
 * @Title: ConsoleGuideServiceImpl.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月12日 time下午7:23:03
 *
 **/
@Service("iConsoleGuideService")
public class ConsoleGuideServiceImpl implements IConsoleGuideService {
	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(ConsoleGuideServiceImpl.class);
	@Resource
	private IGuideDao guideDaoImpl;

	@Override
	public Result<String> add(HttpSession session, GuideEntity g) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
		try {
			User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
			g.setCreatUserName(user.getUserName());
			g.setCheckState(AuditConstants.OPEN_APPLY_UNCHECKED);
			g.setCreatTime(new Date());
			guideDaoImpl.saveOrUpdateGuide(g);
			result.setSuccessMessage("添加成功");
			_LOGGER.info("add guide message success");
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			_LOGGER.error("add guide message faile " + e + "");
			result.setErrorMessage("添加失败");
			return result;
		}
	}

	@Override
	public Result<String> update(GuideEntity g) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
		try {
			_LOGGER.info("update guide message");
			if (guideDaoImpl.getById(g.getId()).getCheckState().equals(AuditConstants.OPEN_APPLY_CHECKED_SUCCESS)) {
				result.setErrorMessage("指南已审核");
				return result;
			} else if (guideDaoImpl.getById(g.getId()).getGuideName().equals(g.getGuideName())) {
				result.setErrorMessage("指南已存在");
				return result;
			} else {
				guideDaoImpl.saveOrUpdateGuide(g);
				result.setSuccessMessage("修改成功");
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			_LOGGER.error("update guide message faile " + e + "");
			result.setErrorMessage("修改失败");
			return result;
		}
	}

	@Override
	public Result<Page<GuideEntity>> guideList(String guideName, String creatUserName, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		Result<Page<GuideEntity>> result = new Result<Page<GuideEntity>>();
		Page<GuideEntity> page = new Page<GuideEntity>(currentPage, 0, pageSize);
		Criteria c = new Criteria();

		if (StringUtils.isNotBlank(guideName)) {

			c.and("guideName").regex(guideName);
		}
		if (StringUtils.isNotBlank(creatUserName)) {

			c.and("creatUserName").regex(creatUserName);
		}
		Query query = new Query(c).with(new Sort(Direction.DESC, "creatTime"));
		result.setData(guideDaoImpl.list(page, query));
		return result;
	}

	@Override
	public Result<String> delete(GuideEntity g) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
		try {
			_LOGGER.info("delete guide message");
			if (guideDaoImpl.getById(g.getId()).getCheckState().equals(AuditConstants.OPEN_APPLY_CHECKED_SUCCESS)) {
				result.setErrorMessage("指南已审核,无法删除");
				return result;
			} else {
				guideDaoImpl.deleteByid(g.getId());
				result.setSuccessMessage("删除成功");
				return result;
			}
		} catch (Exception e) {
			// TODO: handle
			_LOGGER.error("delete guide message faile " + e + "");
			result.setErrorMessage("删除失败");
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
