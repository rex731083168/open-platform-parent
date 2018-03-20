package cn.ce.platform_service.guide.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.guide.dao.IMysqlGuideDao;
import cn.ce.platform_service.guide.entity.GuideEntity;
import cn.ce.platform_service.guide.entity.QueryGuideEntity;
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
@Service("manageGuideService")
@Transactional(propagation=Propagation.REQUIRED)
public class ManageGuideServiceImpl implements IManageGuideService {
	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(ConsoleGuideServiceImpl.class);
//	@Resource
//	private IGuideDao guideDaoImpl;
	@Resource
	private IMysqlGuideDao mysqlGuideDao;

	@Override
	public Result<Page<GuideEntity>> guideList(QueryGuideEntity guideEntity) {
		
		Result<Page<GuideEntity>> result = new Result<Page<GuideEntity>>();
		int totalNum = mysqlGuideDao.findTotalNum(guideEntity);
		List<GuideEntity> list = mysqlGuideDao.getList(guideEntity);
		Page<GuideEntity> page = new Page<GuideEntity>(guideEntity.getCurrentPage(),totalNum,guideEntity.getPageSize(),list);
		result.setSuccessData(page);
		return result;
	}

	@Override
	public Result<String> batchUpdateCheckState(List<String> ids, Integer checkState, String checkMem) {
		// TODO Auto-generated method stub
		Result<String> result = new Result<String>();
//			String message = String.valueOf(guideDaoImpl.bachUpdateGuide(ids, checkState, checkMem));
			int totalNum = mysqlGuideDao.bathUpdateCheckState(ids,checkState,checkMem);
			_LOGGER.info("bachUpdate guide message " + totalNum + " count");
			result.setSuccessMessage("审核成功:" + totalNum + "条");
			return result;
	}

	@Override
	public Result<GuideEntity> getByid(String id) {
		// TODO Auto-generated method stub
		Result<GuideEntity> result = new Result<GuideEntity>();
		//GuideEntity byId = guideDaoImpl.getById(id);
		GuideEntity byId = mysqlGuideDao.findById(id);
		if (null == byId) {
			result.setErrorMessage("当前指南不存在!",ErrorCodeNo.SYS009);
		} else {
			result.setSuccessData(byId);
		}
		return result;
	}

}
