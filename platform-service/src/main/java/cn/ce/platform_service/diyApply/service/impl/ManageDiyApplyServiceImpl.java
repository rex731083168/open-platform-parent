package cn.ce.platform_service.diyApply.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.MongoFiledConstants;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.diyApply.dao.IDiyApplyDao;
import cn.ce.platform_service.diyApply.entity.DiyApplyEntity;
import cn.ce.platform_service.diyApply.entity.appsEntity.Apps;
import cn.ce.platform_service.diyApply.service.IManageDiyApplyService;
import cn.ce.platform_service.guide.entity.GuideEntity;
import cn.ce.platform_service.guide.service.impl.ConsoleGuideServiceImpl;

/**
 * @Description : 说明
 * @Author : makangwei
 * @Date : 2017年10月16日
 */
@Service("manageDiyApplyService")
public class ManageDiyApplyServiceImpl implements IManageDiyApplyService {
	private static Logger _LOGGER = Logger.getLogger(ManageDiyApplyServiceImpl.class);
	@Resource
	private IDiyApplyDao diyApplyDao;

	@Override
	public Result<Page<DiyApplyEntity>> findPagedApps(String productName, String userName, int checkState,
			String applyName, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		Result<Page<DiyApplyEntity>> result = new Result<>();
		Page<DiyApplyEntity> page = new Page<>(currentPage, 0, pageSize);

		Criteria c = new Criteria();
		if (StringUtils.isNotBlank(productName)) {
			c.and("productName").regex(productName);
		}
		if (StringUtils.isNotBlank(userName)) {
			c.and("userName").regex(userName);
		}
		if (StringUtils.isNotBlank(String.valueOf(checkState))) {
			c.and("checkState").is(checkState);
		}
		if (StringUtils.isNotBlank(applyName)) {
			c.and("applyName").regex(applyName);
		}
		Query query = new Query(c).with(new Sort(Direction.DESC, MongoFiledConstants.BASIC_CREATEDATE));
		page = diyApplyDao.findPageByEntity(query, page);
		result.setData(page);

		return result;
	}

	@Override
	public Result<String> batchUpdate(List<String> ids) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
		try {
			String message = String.valueOf(diyApplyDao.bathUpdateByid(ids));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.ce.platform_service.diyApply.service.IManageDiyApplyService#auditUpdate(
	 * java.lang.String, int, java.lang.String)
	 */
	@Override
	public Result<String> auditUpdate(String id, int checkState, String checkMem) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
		try {
			DiyApplyEntity dae = diyApplyDao.findById(id);
			if (dae != null) {
				dae.setCheckState(checkState);
				dae.setCheckMem(checkMem);
				diyApplyDao.saveOrUpdate(dae);
				result.setSuccessMessage("操作成功");
			}
			return result;

		} catch (Exception e) {
			// TODO: handle exception
			_LOGGER.error("auditUpdate failed reason " + e + "");
			result.setErrorMessage("审核失败");
			result.setErrorCode(ErrorCodeNo.SYS001);
			return result;
		}

	}

}
