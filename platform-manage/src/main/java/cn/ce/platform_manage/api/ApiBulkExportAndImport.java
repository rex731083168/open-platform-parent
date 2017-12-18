package cn.ce.platform_manage.api;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.ce.platform_service.apis.entity.ApiExportParamEntity;
import cn.ce.platform_service.apis.service.IApiTransportService;
import cn.ce.platform_service.common.AuditConstants;
import cn.ce.platform_service.common.Constants;
import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.util.PageValidateUtil;

/**
* @Description : api在不同环境的批量导出功能
* @Author : makangwei
* @Date : 2017年12月6日
*/
@RestController
@RequestMapping(value="/apiBulk")
public class ApiBulkExportAndImport {

    @Resource
    private IApiTransportService apiTransportService;
    
	private static Logger _LOGGER = LoggerFactory.getLogger(ApiBulkExportAndImport.class);
	
	/**
	 * @Description: 导出文件
	 * @author: makangwei 
	 * @date:   2017年12月12日 下午6:23:06 
	 */
	@RequestMapping(value="/exportApis",method=RequestMethod.GET)
	public String exportApis(HttpServletRequest request, HttpServletResponse response, String recordId){
		// TODO 20171211 mkw 虽然返回的是string，但是实际String并不起作用
		_LOGGER.info("批量导出api文档");
		
		return apiTransportService.exportApis(recordId, response);
	}
	
	/**
	 * 
	 * @Title: generalExportList
	 * @Description: 生成导出记录
	 * @author: makangwei 
	 * @date:   2017年12月12日 下午6:22:43 
	 */
	@RequestMapping(value="/generalExportList", method=RequestMethod.POST)
	public Result<?> generalExportList(HttpServletRequest request,@RequestBody ApiExportParamEntity exportParam){
		
		_LOGGER.info("生成批量导出api的文件记录");
		
		if(exportParam.getApiIds() == null){
			exportParam.setApiIds(new ArrayList<String>());
		}if(exportParam.getAppIds() == null){
			exportParam.setAppIds(new ArrayList<String>());
		}
		
		User user = null;
		try{
			user = (User)request.getSession().getAttribute(Constants.SES_LOGIN_USER);
		}catch(Exception e){
			Result.errorResult("", ErrorCodeNo.SYS003, null, Status.FAILED);
		}
		return apiTransportService.generalExportList(exportParam, user);
	}
	
	@RequestMapping(value="/importApis", method=RequestMethod.POST)
	public Result<?> importApis(HttpServletRequest request, @RequestParam("file") MultipartFile file){
		User user = null;
		try{
			user = (User)request.getSession().getAttribute(Constants.SES_LOGIN_USER);
		}catch(Exception e){
			Result.errorResult("", ErrorCodeNo.SYS003, null, Status.FAILED);
		}
		_LOGGER.info("文件上传");
		
		byte[] b= null;
		String upStr = null;
		if(!file.getOriginalFilename().endsWith(".json")){
			return Result.errorResult("文件格式不正确", ErrorCodeNo.DOWNLOAD003, "", Status.FAILED);
		}
		try {
			b = file.getBytes();
			long size = file.getSize();
			if(size > AuditConstants.MAX_UPLOAD_SIZE){
				Result.errorResult("文件大小不能超过2M", ErrorCodeNo.UPLOAD002, "'", Status.FAILED);
			}
			upStr = new String(b,"utf-8");
		} catch (IOException e) {
			_LOGGER.info("上传文件读取数据发生异常");
			Result.errorResult("", ErrorCodeNo.SYS004, null, Status.FAILED);
		}
		
		if(upStr.length() > 1000){
			
		}
		
		return apiTransportService.importApis(upStr, user);
	}
	
	@RequestMapping(value="getExportRecords", method=RequestMethod.GET)
	public Result<?> getExportRecords(HttpServletRequest request, HttpServletResponse response, 
			//@RequestBody DApiRecordEntity dRecordEntity,
			@RequestParam(required=false,defaultValue= "1") int currentPage, 
			@RequestParam(required=false,defaultValue= "10")int pageSize){
	
		
		return apiTransportService.getExportRecords(PageValidateUtil.checkCurrentPage(currentPage),PageValidateUtil.checkPageSize(pageSize));
	}
	
	@RequestMapping(value="getExportRecordById", method=RequestMethod.GET)
	public Result<?> getExportRecordById(String recordId){
		
		return apiTransportService.getExportRecordById(recordId);
	}
	
	@RequestMapping(value="/getImportRecords", method=RequestMethod.GET)
	public Result<?> getImportRecords(
			@RequestParam(required=false,defaultValue= "1") int currentPage, 
			@RequestParam(required=false,defaultValue= "10")int pageSize){
		
		return apiTransportService.getImportRecords(PageValidateUtil.checkCurrentPage(currentPage),PageValidateUtil.checkPageSize(pageSize));
	}
	
	@RequestMapping(value="/getImportRecordById", method=RequestMethod.GET)
	public Result<?> getImportRecordById(String recordId){
		
		return apiTransportService.getImportRecordById(recordId);
	}
	
}
