package cn.ce.platform_service.dubbapply.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.HttpClientUtil;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.dubbapply.dao.DubboApplyEntityMapper;
import cn.ce.platform_service.dubbapply.entity.Interfaceapplyentity.Data;
import cn.ce.platform_service.dubbapply.entity.Interfaceapplyentity.DubboApps;
import cn.ce.platform_service.dubbapply.service.IDubboApplyService;
import cn.ce.platform_service.util.PropertiesUtil;
/***
 * dubbo jar解析服务
 * @author huangdayang
 * 2018年3月16日
 */
@Service("dubboApplyService")
public class DubboApplyServiceImpl implements IDubboApplyService {
	/** 日志对象 */
	private static Logger _LOGGER = Logger.getLogger(DubboApplyServiceImpl.class);
	@Resource
	private DubboApplyEntityMapper dubboApplyEntityMapper;
//	@Resource
//	private IDubboApplyDao dubboApplyDao;

//	private void saveDubboApplySercice(List<DubboApplyEntity> dubboApplyEntityList) {
//		for (DubboApplyEntity record : dubboApplyEntityList) {
//			dubboApplyEntityMapper.insert(record);
//		}
//	}
//
//	@Override
//	public boolean saveDubboApplyService(String path, String[] dependencyJarName, String mainJarName) {
//		
//		List<String> list = new ArrayList<String>();
//		
//		list.add(path+File.separator+mainJarName);
//		if(dependencyJarName != null) {
//			for (String string : dependencyJarName) {
//				list.add(path+File.separator+string);
//			}
//		}
//		String[] jarpath =list.toArray(new String[] {});
//		Map<String, Map<String, InterfaceDescriptionFullEnty>> simpleInfo= null;
//		try {
//			// TODO
//			simpleInfo = ModuleClassLoader.getInstance().parseJars("sss",jarpath);
//		} catch (ClassNotFoundException e) {
//			_LOGGER.error("class load ClassNotFoundException", e);
//		} catch (URISyntaxException e) {
//			_LOGGER.error("class load URISyntaxException", e);
//		} catch (IOException e) {
//			_LOGGER.error("class load IOException", e);
//		}
//		if(simpleInfo == null) return false;
//		int c = this.countMainJar(simpleInfo);
//		if(c != 1) {
//			_LOGGER.error("main jar must only one,but now is :"+c);
//			return false;
//		};
//		
//		
//		
//		return false;
//	}
//	/**
//	 * 统计main jar的数量
//	 * @param simpleInfo
//	 * @return
//	 */
//	private int countMainJar(Map<String, Map<String, InterfaceDescriptionFullEnty>> simpleInfo) {
//		int count = 0;
//		for (String jarName : simpleInfo.keySet()) {
//			if(simpleInfo.get(jarName).size() >=  1) {
//				count++;
//			}
//		}
//		return count;
//		
//	}
//    /**
//     * 处理解析信息
//     * @param simpleInfo
//     * @return
//     */
//	private List<DubboApplyEntity> dealInterfaceDescriptionFullEnty(Map<String, Map<String, InterfaceDescriptionFullEnty>> simpleInfo) {
//		List<DubboApplyEntity> list = new ArrayList<DubboApplyEntity>();
//		String rootId = UUID.randomUUID().toString();
//		for (String jarName : simpleInfo.keySet()) {
//			if(simpleInfo.get(jarName).size() >=  1) {
//				 list.addAll(convertMainJar(rootId,jarName,simpleInfo.get(jarName)));
//			} else {
//				//dependency 处理依赖包 
//				DubboApplyEntity dae = new DubboApplyEntity();
//				dae.setAppcode("");
//				dae.setAppid("");
//				dae.setAppname("");
//				dae.setFilepath("");
//				dae.setFiletag("2"); //dependency
//				dae.setFilename(jarName);
//				dae.setCreattime(new Date());
//				dae.setUpdattime(new Date());
//				dae.setCreatuserid("");
//				dae.setParentid(rootId);
//				list.add(dae);
//			}
//		}
//		return list;
//	}
//
//	
//	private List<DubboApplyEntity> convertMainJar(String rootId,String jarName,Map<String, InterfaceDescriptionFullEnty> mainJarInfo) {
//		List<DubboApplyEntity> list = new ArrayList<DubboApplyEntity>();
//		//处理 jar rootId
//		DubboApplyEntity root = new DubboApplyEntity();
//		root.setAppcode("");
//		root.setId(rootId);
//		root.setAppid("");
//		root.setAppname("");
//		root.setFilepath("");
//		root.setFiletag("1"); //service
//		root.setFilename(jarName);
//		root.setCreattime(new Date());
//		root.setUpdattime(new Date());
//		root.setCreatuserid("");
//		root.setParentid(null);
//		list.add(root);
//	    //处理 class 
//		Map<String,String> classMap = new HashMap<String,String>(); 
//		for (InterfaceDescriptionFullEnty enty : mainJarInfo.values()) {
//			String parentId = classMap.get(enty.getClassname());
//			if(parentId == null) {
//				parentId = UUID.randomUUID().toString();
//				classMap.put(enty.getClassname(),parentId);
//			};
//			DubboApplyEntity dae = new DubboApplyEntity();
//			dae.setAppcode("");
//			dae.setAppid("");
//			dae.setAppname("");
//			dae.setFilepath("");
//			dae.setFiletag("1"); //service
//			dae.setFilename(jarName);
//			dae.setCreattime(new Date());
//			dae.setUpdattime(new Date());
//			dae.setParentid(null);         
//			dae.setCreatuserid("");
//		}
//		return list;
//		
//	}

	@Override
	public Result<?> findAppsByUnit(String unit, String appName, Integer currentPage, Integer pageSize) {
		
		String url = PropertiesUtil.getInstance().getValue("dubbo_app_interfaceurl");
		String unit$ = Pattern.quote("${unit}");// ${o} 所属企业 CE 为中企动力 不填为所有
		String replacedurl = url.replaceAll(unit$, unit);
		_LOGGER.info("请求产品中心Url:" + url);
		Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
		classMap.put("list", cn.ce.platform_service.diyApply.entity.appsEntity.AppList.class);
		classMap.put("appTypes", cn.ce.platform_service.diyApply.entity.appsEntity.AppTypes.class);

		try {
			/* get请求方法 */
			DubboApps apps = (DubboApps) HttpClientUtil.getUrlReturnObject(replacedurl, DubboApps.class, classMap);
			_LOGGER.info("调用产品Http请求发送成功");
			if (apps.getStatus().equals("200") || apps.getStatus().equals("110")) {
				//分页，模糊
				List<Data> reData = apps.getData();
				Page<Data> page = adaptAppData(reData,appName,currentPage,pageSize);
				return new Result<Page<Data>>("",ErrorCodeNo.SYS000,page,Status.SUCCESS);
			} else {
				_LOGGER.error("dubbo_app_interfaceurl data http getfaile return code :" + apps.getMsg() + " ");
				return new Result<Page<Data>>("",ErrorCodeNo.SYS006,null,Status.FAILED);
			}
		} catch (Exception e) {
			_LOGGER.error("dubbo_app_interfaceurl http error " + e + "");
			return new Result<Page<Data>>("",ErrorCodeNo.SYS018,null,Status.FAILED);
		}
	}

	
	/**
	 * 
	 * @Title: adaptAppData
	 * @Description: 王佳的接口不支持适配。通过该方法支持分页和模糊搜索的功能
	 * @author: makangwei 
	 * @date:   2018年4月28日 上午11:36:11 
	 * @param : @param reData
	 * @param : @param appName
	 * @param : @param currentPage
	 * @param : @param pageSize
	 * @param : @return
	 * @return: List<Data>
	 * @throws
	 */
	private Page<Data> adaptAppData(List<Data> reData, String appName, Integer currentPage, Integer pageSize) {
		
		if(null == reData || reData.size() < 1){
			return new Page<Data>(currentPage,0,pageSize);
		}
		
		//根据名称模糊查询
		if(StringUtils.isNotBlank(appName)){
			for (Data data : reData) {
				if(data.getAppName().indexOf(appName) < 0){
					reData.remove(data);
				}
			}
		}
		
		//分页
		List<Data> newData = new ArrayList<Data>();
		int begin = (currentPage-1)*pageSize;
		int end = currentPage*pageSize;
		for(int i = begin ; i< end ; i++){
			// TODO 处理越界异常
			newData.add(reData.get(i));
		}
		Page<Data> page = new Page<Data>(currentPage,reData.size(),pageSize,newData);
		return page;
	}

}
