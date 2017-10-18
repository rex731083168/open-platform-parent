package cn.ce.platform_service.guide.service.impl;

import java.util.Date;
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

import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.MongoFiledConstants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.guide.dao.IGuideDao;
import cn.ce.platform_service.guide.entity.GuideEntity;
import cn.ce.platform_service.guide.service.IConsoleGuideService;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.util.SplitUtil;

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

			Query query = new Query(Criteria.where("guideName").is(g.getGuideName()));

			List<GuideEntity> list = guideDaoImpl.list(query);

			if (list != null && list.size() > 0) {
				result.setErrorCode(ErrorCodeNo.SYS009);
				result.setErrorMessage("指南已存在");
				return result;
			}

			User user = (User) session.getAttribute(Constants.SES_LOGIN_USER);
			g.setCreatUserName(user.getUserName());
			g.setCreatTime(new Date());
			g.setCheckState(AuditConstants.GUIDE_UNCHECKED);
			guideDaoImpl.saveOrUpdateGuide(g);
			result.setSuccessMessage("添加成功");
			_LOGGER.info("add guide message success");
			return result;

		} catch (Exception e) {
			// TODO: handle exception
			_LOGGER.error("add guide message faile " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
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

			GuideEntity ge = guideDaoImpl.getById(g.getId());

			if (ge == null) {
				result.setErrorCode(ErrorCodeNo.SYS015);
				result.setMessage("指南不存在!");
				return result;
			}

			Query query = new Query(
					Criteria.where("guideName").is(g.getGuideName()).and(MongoFiledConstants.BASIC_ID).ne(g.getId()));

			List<GuideEntity> list = guideDaoImpl.list(query);

			if (list != null && list.size() > 0) {
				result.setMessage("指南名称重复!");
				result.setErrorCode(ErrorCodeNo.SYS010);
				return result;
			}

			if (AuditConstants.GUIDE_SUCCESS == ge.getCheckState()) {
				result.setErrorMessage("指南已审核!");
				return result;
			}

			guideDaoImpl.saveOrUpdateGuide(g);
			result.setSuccessMessage("修改成功");
			return result;

		} catch (Exception e) {
			// TODO: handle exception
			_LOGGER.error("update guide message faile " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("修改失败");
			return result;
		}
	}

	@Override
	public Result<Page<GuideEntity>> guideList(GuideEntity entity, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		Result<Page<GuideEntity>> result = new Result<Page<GuideEntity>>();
		Page<GuideEntity> page = new Page<GuideEntity>(currentPage, 0, pageSize);
		Criteria c = new Criteria();

		if (StringUtils.isNotBlank(entity.getGuideName())) {
			c.and("guideName").regex(entity.getGuideName());
		}
		if (StringUtils.isNotBlank(entity.getCreatUserName())) {
			c.and("creatUserName").regex(entity.getCreatUserName());
		}
		if (StringUtils.isNotBlank(entity.getCreatUserName())) {
			c.and("appIyId").is(entity.getCreatUserName());
		}

		Query query = new Query(c).with(new Sort(Direction.DESC, MongoFiledConstants.BASIC_CREATEDATE));
		result.setSuccessData(guideDaoImpl.listByPage(page, query));
		return result;
	}

	@Override
	public Result<String> delete(String id) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
		try {
			_LOGGER.info("delete guide message");

			GuideEntity ge = guideDaoImpl.getById(id);

			if (ge == null) {
				result.setErrorCode(ErrorCodeNo.SYS015);
				result.setMessage("指南不存在!");
				return result;
			}

			if (AuditConstants.GUIDE_SUCCESS == ge.getCheckState()) {
				result.setErrorMessage("指南已审核,无法删除!");
				return result;
			}

			if (AuditConstants.OPEN_APPLY_CHECKED_COMMITED == ge.getCheckState()) {
				result.setErrorMessage("指南审核中,无法删除!");
				return result;
			}

			guideDaoImpl.deleteByid(id);

			result.setSuccessMessage("删除成功");
			return result;

		} catch (Exception e) {
			// TODO: handle
			_LOGGER.error("delete guide message faile " + e + "");
			result.setErrorCode(ErrorCodeNo.SYS001);
			result.setErrorMessage("删除失败");
			return result;
		}
	}

	@Override
	public Result<GuideEntity> getByid(String id) {
		// TODO Auto-generated method stub
		Result<GuideEntity> result = new Result<GuideEntity>();

		GuideEntity byId = guideDaoImpl.getById(id);

		if (byId == null) {
			result.setErrorCode(ErrorCodeNo.SYS015);
			result.setMessage("指南不存在!");
			return result;
		}
		result.setSuccessData(byId);
		return result;
	}

	@Override
	public Result<String> submitVerify(String id) {
		Result<String> result = new Result<>();
		List<String> asList = SplitUtil.splitStringWithComma(id);
		try {
			String message = String.valueOf(guideDaoImpl.bachUpdateGuide(asList,AuditConstants.GUIDE_COMMITED, ""));
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

}
