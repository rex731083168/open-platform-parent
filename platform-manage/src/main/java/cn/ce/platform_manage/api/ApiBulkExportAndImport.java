package cn.ce.platform_manage.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import cn.ce.platform_service.apis.entity.ApiEntity;
import cn.ce.platform_service.apis.entity.ApiExportParamEntity;
import cn.ce.platform_service.apis.service.IManageApiService;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.util.SplitUtil;

/**
* @Description : api在不同环境的批量导出功能
* @Author : makangwei
* @Date : 2017年12月6日
*/
@Controller
@RequestMapping(value="/apiBulk")
public class ApiBulkExportAndImport {

    @Resource
    private IManageApiService manageApiService;
    
	private static Logger _LOGGER = LoggerFactory.getLogger(ApiBulkExportAndImport.class);
	
	@RequestMapping(value="/exportApis",method=RequestMethod.GET)
	public String exportApis(HttpServletRequest request, HttpServletResponse response, String apiIds, String appIds){
		// TODO 20171211 mkw 虽然返回的是string，但是实际String并不起作用
		_LOGGER.info("批量导出api文档");
		
		return manageApiService.exportApis(SplitUtil.splitStringWithComma(apiIds), response);
	}
	
	@RequestMapping(value="/generalExportList")
	public Result<?> generalExportList(ApiExportParamEntity exportParam){
		
		_LOGGER.info("生成批量导出api的文件记录");
		
		return manageApiService.generalExportList(exportParam);
	}
	@RequestMapping(value="/testExport", method=RequestMethod.GET)
	public String testExport(HttpServletResponse response,@RequestParam(required=false)int id) throws IOException{
		if(id == 1){
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			response.getOutputStream().write(JSONObject.toJSONString(Result.errorResult("错误啦", ErrorCodeNo.SYS001, "aaa", Status.FAILED)).getBytes("utf-8"));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		
		if(id==2){
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			response.getOutputStream().write(JSONObject.toJSONString(Result.errorResult("错误啦2", ErrorCodeNo.SYS001, "aaa", Status.FAILED)).getBytes("utf-8"));
			response.getOutputStream().flush();
			response.getOutputStream().close();
			return "bbb";
		}
		List<ServerAddress> list1 = new ArrayList<ServerAddress>();
		list1.add(new ServerAddress("10.12.40.83",23000));
		list1.add(new ServerAddress("10.12.40.84",23000));
		list1.add(new ServerAddress("10.12.40.86",23000));
		List<MongoCredential> list2 = new ArrayList<MongoCredential>();
		list2.add(MongoCredential.createCredential("makangwei", "admin","123456".toCharArray()));
		MongoClient client1 = new MongoClient(list1,list2);
		MongoOperations mongoOps = new MongoTemplate(client1,"openplatform");
		List<ApiEntity> list = mongoOps.findAll(ApiEntity.class);
		System.out.println(list.size());
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment; filename=down.json");
		response.getOutputStream().write(JSONObject.toJSONString(list).getBytes("utf-8"));
		response.getOutputStream().flush();
		response.getOutputStream().close();
		return "aaa";
	}
	
}
