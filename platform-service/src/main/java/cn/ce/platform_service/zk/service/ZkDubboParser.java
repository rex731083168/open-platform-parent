package cn.ce.platform_service.zk.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.ce.platform_service.zk.entity.DubboConfigurator;
import cn.ce.platform_service.zk.entity.DubboConsumer;
import cn.ce.platform_service.zk.entity.DubboProvider;
import cn.ce.platform_service.zk.entity.DubboRouter;

/**
 * @Description : zk中dubbo服务的解析工具类
 * @Author : makangwei
 * @Date : 2018年1月9日
 */
public class ZkDubboParser{
	
	private static final Logger _LOGGER = LoggerFactory.getLogger(ZkDubboParser.class);
	
	public static DubboProvider parserProvider (String data) {
		// 将data执行utf-8的decoded解码
		try{
			data = URLDecoder.decode(data, "UTF-8");
		}catch(UnsupportedEncodingException e){
			_LOGGER.info("url decoded 错误");
			// TODO
			return null;
		}
		_LOGGER.info("org:"+data);
		String[] tempDataArray = data.split("\\?", 2);
		if (tempDataArray.length != 2) {
			_LOGGER.info("dubbo数据解析错误");
			 // TODO 
			return null;
		}

		String protocol = tempDataArray[0].split("://")[0];
		String uri = tempDataArray[0].split("://")[1].split("/")[0];

		Map<String, String> params = getUrlPram(tempDataArray[1]);
		
		// TODO 20180109 mkw 根据effective java 2 进行优化
		DubboProvider provider = new DubboProvider();
		if(StringUtils.isNotBlank(uri)){
			provider.setUri(uri);
		}
		if(StringUtils.isNotBlank(protocol)){
			provider.setProtocol(protocol);
		}
		if(StringUtils.isNotBlank(params.get("application"))){
			provider.setApplication(params.get("application"));
		}
		if(StringUtils.isNotBlank(params.get("path"))){
			provider.setPath(params.get("path"));
		}
		if(StringUtils.isNotBlank(params.get("group"))){
			provider.setGroup(params.get("group"));
		}
		if(StringUtils.isNotBlank(params.get("version"))){
			provider.setVersion(params.get("version"));
		}
		if(StringUtils.isNotBlank(params.get("dubbo"))){
			provider.setDubboVersion(params.get("dubbo"));
		}
		if(StringUtils.isNotBlank(params.get("token"))){
			provider.setToken(params.get("token"));
		}
		if(StringUtils.isNotBlank(params.get("timeout"))){
			provider.setTimeOut(params.get("timeout"));
		}
		if(StringUtils.isNotBlank(params.get("owner"))){
			provider.setOwner(params.get("owner"));
		}
		if(StringUtils.isNotBlank(params.get("interface"))){
			provider.setInterfaceName(params.get("interface"));
		}
		if(StringUtils.isNotBlank(params.get("timestamp"))){
			provider.setTimeStamp(params.get("timestamp"));
		}
		if(StringUtils.isNotBlank(params.get("loadbalance"))){
			provider.setLoadBalance(params.get("loadbalance"));
		}
		if(StringUtils.isNotBlank(params.get("methods"))){
			provider.setMethods(params.get("methods").split(","));
		}

		_LOGGER.info("aft:"+provider);
		return provider;
	}

	public static DubboConsumer parserConsumer(String data) {
		// 将data执行utf-8的decoded解码
		try{
			data = URLDecoder.decode(data, "UTF-8");
		}catch(UnsupportedEncodingException e){
			_LOGGER.info("url decoded 错误");
			// TODO
			return null;
		}
		_LOGGER.info("org:"+data);

		String[] tempDataArray = data.split("\\?", 2);
		if (tempDataArray.length != 2) {
			_LOGGER.info("dubbo数据解析错误");
			 // TODO 
			return null;
		}
		
		String uri = tempDataArray[0].split("://")[1].split("/")[0];

		Map<String, String> params = getUrlPram(tempDataArray[1]);
		
		// TODO 20180109 mkw 根据effective java 2 进行优化
		DubboConsumer consumer = new DubboConsumer();
		consumer.setUri(uri);
		if(StringUtils.isNotBlank(params.get("application"))){
			consumer.setApplication(params.get("application"));
		}
		if(StringUtils.isNotBlank(params.get("catetory"))){
			consumer.setCatetory(params.get("catetory"));
		}
		if(StringUtils.isNotBlank(params.get("default.ThreadPoolCoreSize"))){
			consumer.setThradPoolCoreSize(params.get("default.ThreadPoolCoreSize"));
		}
		if(StringUtils.isNotBlank(params.get("interface"))){
			consumer.setInterfaceName(params.get("interface"));
		}
		if(StringUtils.isNotBlank(params.get("methods"))){
			consumer.setMethods(params.get("methods").split(","));
		}
		_LOGGER.info("aft:"+consumer);
		return consumer;
	}

	private static Map<String, String> getUrlPram(String urlParam) {

		Map<String, String> params = new HashMap<String, String>();
		String[] arrSplit = urlParam.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");
			// 解析出键值
			if (arrSplitEqual.length > 1) {
				// 正确解析
				params.put(arrSplitEqual[0], arrSplitEqual[1]);

			} else {
				if (arrSplitEqual[0] != "") {
					// 只有参数没有值，不加入
					params.put(arrSplitEqual[0], "");
				}
			}
		}
		return params;
	}
	

	public static DubboRouter parserRouter(String data) {
		// 将data执行utf-8的decoded解码
		try{
			data = URLDecoder.decode(data, "UTF-8");
		}catch(UnsupportedEncodingException e){
			_LOGGER.info("url decoded 错误");
			// TODO
			return null;
		}

		DubboRouter router = new DubboRouter();
		router.setDesc(data);
		
		return router;
	}

	public static DubboConfigurator parserConfigurators(String data) {
		// 将data执行utf-8的decoded解码
		try{
			data = URLDecoder.decode(data, "UTF-8");
		}catch(UnsupportedEncodingException e){
			_LOGGER.info("url decoded 错误");
			// TODO
			return null;
		}

		DubboConfigurator configurator = new DubboConfigurator();
		configurator.setDesc(data);
		
		return configurator;
	}

//	public static void main(String[] args) {
//		String url = "/dubbo/cn.ce.ebiz.complaintPage.service.ComplaintPageContentService/consumers/consumer://172.20.0.29/cn.ce.ebiz.complaintPage.service.ComplaintPageContentService?application=front&category=consumers&check=false&default.check=false&default.reference.filter=cecontext&default.retries=1&default.timeout=5000&dubbo=2.5.4-SNAPSHOT&interface=cn.ce.ebiz.complaintPage.service.ComplaintPageContentService&methods=findListByFront,getRelatedIds,findContentListByRelatedIds,findComplaintPageRelatedByType,findComplaintPageContentPageList,getBreadcrumbData,deleteCategory,findContentList,getComplaintPageContentByID,getSimpleTitle,findByPagination,updateComplaintPageContent,getById,updateShortUrl,deleteComplaintPageBigField,getSearchTips,deleteComplaintPageRelatedByType,saveComplaintPageRelated,dragSortComplaintAndCate,findMobileContentList,saveComplaintPageContent,findComplaintPageAdmixDataPageList,getDefaultParamId,findRelationVoList,updateContentState,findListComplaintPageContent,deleteComplaintPageRelated,getSemanticValue,updateBatch,saveComplaintPageRelatedByType,getCatCount,findFrontComplaintContent,deleteComplaintPageContent,getAppIds&owner=ebiz&pid=21378&revision=1.0.0-SNAPSHOT&side=consumer&timestamp=1515378543512";
//		System.out.println(ZkDubboParser.parserConsumer(url));
//		String url2= "/dubbo/cn.ce.open.complaintPage.service.AppComplaintPageCategoryService/providers/dubbo://10.20.0.122:20881/cn.ce.open.complaintPage.service.AppComplaintPageCategoryService?anyhost=true&application=sc-site&default.service.filter=cecontext&dubbo=2.5.4-SNAPSHOT&generic=false&interface=cn.ce.open.complaintPage.service.AppComplaintPageCategoryService&loadbalance=roundrobin&methods=deleteCategoryAndImageUtilization,saveImportData,updateCategoryAndImageUtilization,saveCategoryAndImageUtilization&owner=ebiz&pid=6659&revision=1.0.0-SNAPSHOT&side=provider&timeout=30000&timestamp=1512644754014";
//		System.out.println(ZkDubboParser.parserProvider(url2));
//	}

}
