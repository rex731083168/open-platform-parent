//package cn.ce.platform_console.data_migra;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.bson.Document;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.alibaba.fastjson.JSON;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoCredential;
//import com.mongodb.ServerAddress;
//import com.mongodb.client.FindIterable;
//
//import cn.ce.platform_service.apis.service.IConsoleApiService;
//import cn.ce.platform_service.common.Result;
//import cn.ce.platform_service.diyApply.service.IConsoleDiyApplyService;
//import cn.ce.platform_service.gateway.entity.SaasEntity;
//import cn.ce.platform_service.gateway.service.IGatewayManageService;
//import cn.ce.platform_service.gateway.service.ISaasService;
//import cn.ce.platform_service.gateway.service.ISaasService1;
//import cn.ce.platform_service.guide.service.IConsoleGuideService;
//import cn.ce.platform_service.openApply.service.IConsoleOpenApplyService;
//import cn.ce.platform_service.users.service.IConsoleUserService;
//import cn.ce.platform_service.util.PropertiesUtil;
//
///**
//* @Description : 说明
//* @Author : makangwei
//* @Date : 2018年1月17日
//*/
//@RestController
//@RequestMapping("/data/migra")
//@Transactional
//public class DataMigraController {
//
//	@Resource
//	private IConsoleUserService consoleUserService;
//	@Resource
//	private IConsoleApiService consoleApiService;
//	@Resource
//	private IConsoleGuideService consoleGuideGuide;
//	@Resource
//	private IConsoleDiyApplyService consoleDiyApplyService;
//	@Resource
//	private IConsoleOpenApplyService consoleOpenApplyService;
//	@Resource
//	private IGatewayManageService gatewayManageService;
//	@Resource
//	private ISaasService1 saasService1;
//	@Resource
//	private ISaasService saasService;
//	
//	@RequestMapping(value="/user", method=RequestMethod.GET)
//	public Result<?> migraUser(){
//		
//		return consoleUserService.migraUser();
//	}
//	
//	@RequestMapping(value="/api", method=RequestMethod.GET)
//	public Result<?> migraApi(){
//		
//		return consoleApiService.migraApi();
//	}
//	
//	@RequestMapping(value="/guide", method=RequestMethod.GET)
//	public Result<?> migraGuide(){
//		
//		return consoleGuideGuide.migraGuide();
//	}
//	
//	@RequestMapping(value="/diyApply", method=RequestMethod.GET)
//	public Result<?> migraDiyApply(){
//		
//		return consoleDiyApplyService.migraDiyApply();
//	}
//	
//	@RequestMapping(value="/openApply", method=RequestMethod.GET)
//	public Result<?> migraOpenApply(){
//		
//		return consoleOpenApplyService.migraOpenApply();
//	}
//	
//	@RequestMapping(value="/gateway", method=RequestMethod.GET)
//	public Result<?> migraGateway(){
//		
//		return gatewayManageService.migraGateway();
//	}
//	
//	//将所有saas的数据迁移到开放平台
//	@RequestMapping(value="/saas", method=RequestMethod.GET)
//	public Result<?> migraSaas(){
//		
//		// mongo environment config
//		String mongoSet = PropertiesUtil.getInstance().getValue("mongodb.replica-set");
//		//mongo url:port
//		List<ServerAddress> seeds = new ArrayList<ServerAddress>();
//		for (String set : mongoSet.split(",")) {
//			ServerAddress address = new ServerAddress(set);
//			seeds.add(address);
//		}
//		//mongo credential
//		List<MongoCredential> credentials = new ArrayList<MongoCredential>();
//		MongoCredential cred = MongoCredential.createCredential("openplatform", "admin", "q1w2e3r4ys".toCharArray());
//		credentials.add(cred);
//		MongoClient mongo = new MongoClient(seeds,credentials);
//		System.out.println(mongo.toString());
//		FindIterable<Document> docs = mongo.getDatabase("tyk").getCollection("saas").find();
//		int i = 0;
//		saasService.clearAll();
//		for (Document doc : docs) {
//			SaasEntity saas = JSON.parseObject(doc.toJson(), SaasEntity.class);
//			saasService1.migraSaas(saas);
//			i++;
//		}
//		mongo.close();
//		Result<String> result = new Result<String>();
//		result.setSuccessMessage("一共迁移了"+i+"条saas数据");
//		return result;
//	}
//
//	@RequestMapping(value="/queryArgs", method=RequestMethod.GET)
//	public Result<?> migraQueryArgs(){
//		
//		return consoleApiService.migraQueryArgs();
//	}
//	
//}
