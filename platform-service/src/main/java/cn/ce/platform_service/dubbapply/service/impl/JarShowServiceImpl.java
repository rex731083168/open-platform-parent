package cn.ce.platform_service.dubbapply.service.impl;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.common.page.Page;
import cn.ce.platform_service.dubbapply.dao.IDubboDepJarDao;
import cn.ce.platform_service.dubbapply.dao.IDubboMainJarDao;
import cn.ce.platform_service.dubbapply.entity.DepJar;
import cn.ce.platform_service.dubbapply.entity.MainJar;
import cn.ce.platform_service.dubbapply.entity.QueryDepJar;
import cn.ce.platform_service.dubbapply.entity.QueryMainJar;
import cn.ce.platform_service.dubbapply.service.IJarShowService;
import cn.ce.platform_service.dubbapply.util.JarDfsUtil;
import cn.ce.platform_service.util.ModuleClassLoader;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年4月28日
*/
@Service("jarShowService")
public class JarShowServiceImpl implements IJarShowService{

	private static final Logger _LOGGER = LoggerFactory.getLogger(JarShowServiceImpl.class);
	
	@Resource
	IDubboMainJarDao dubboMainJarDao;
	@Resource
	IDubboDepJarDao dubboDepJarDao;
	
	@Override
	public Result<?> downLoadJar(String mainJarId, HttpServletResponse response) {
		
		if(StringUtils.isBlank(mainJarId)){
			return new Result<String>("主包id不能为空",ErrorCodeNo.SYS005,null,Status.FAILED);
		}
		MainJar mainJar = dubboMainJarDao.findMainJarById(mainJarId);
		if(null == mainJar){
			return new Result<String>("当前id不存在",ErrorCodeNo.SYS006,null,Status.FAILED);
		}
		//从fastdfs中将所有的文件读取出来
		byte[] mainJarFile;
		try {
			mainJarFile = JarDfsUtil.getInstance().readFile(mainJar.getDfsPath());
		} catch (Exception e1) {
			e1.printStackTrace();
			_LOGGER.error("当前主包"+mainJar.getOriginalFileName()+"的fastdfs主键"+mainJar.getDfsPath()+"在fastdfs中不存在");
			return new Result<String>("fasfdfs部分数据丢失或不存在",ErrorCodeNo.DOWNLOAD004,null,Status.FAILED);
		}
		List<DepJar> depJars = dubboDepJarDao.findDepJarsByMainId(mainJar.getId());
		Map<String,byte[]> childrenFile = null;
		
		if(null != depJars && depJars.size() > 0){
			childrenFile = new HashMap<String, byte[]>();
			for (DepJar depJar : depJars) {
				byte[] depJarBytes;
				try {
					depJarBytes = JarDfsUtil.getInstance().readFile(depJar.getDfsPath());
				} catch (Exception e) {
					//必须全部拿到。否则发生异常返回错误
					e.printStackTrace();
					_LOGGER.error("当前从包"+mainJar.getOriginalFileName()+"下的从包"+depJar.getOriginalFileName()+"的fastdfs主键"+depJar.getDfsPath()+"在fastdfs中不存在");
					return new Result<String>("fasfdfs部分数据丢失或不存在",ErrorCodeNo.DOWNLOAD004,null,Status.FAILED);
				}
				childrenFile.put(depJar.getOriginalFileName(), depJarBytes);
			}
		}
		
		String zipPath = null;
		try {
			zipPath = new File("").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String zipName = mainJar.getOriginalFileName()+"_download_"+SimpleDateFormat.getTimeInstance()+".zip";
		String mainJarName = mainJar.getOriginalFileName();
		try {
			createDownloadZip(zipPath, zipName, mainJarName,mainJarFile,childrenFile, response);
		} catch (IOException e) {
			e.printStackTrace();
			return new Result<String>("下载jar包发生异常。",ErrorCodeNo.DOWNLOAD004,null,Status.FAILED);
		}
		return null;
	}

	@Override
	public Result<?> parseMainJar(String mainJarId){
		
		try {
			if(StringUtils.isBlank(mainJarId)){
				return new Result<String>("主包id不能为空",ErrorCodeNo.SYS005,null,Status.FAILED);
			}
			MainJar mainJar = dubboMainJarDao.findMainJarById(mainJarId);
			if(null == mainJar){
				return new Result<String>("当前id不存在",ErrorCodeNo.SYS006,null,Status.FAILED);
			}
			byte[] mainJarFile = JarDfsUtil.getInstance().readFile(mainJar.getDfsPath());
			File f = new File(this.getClass().getResource("/").getPath(),mainJar.getOriginalFileName());
			if(!f.exists()){
				f.createNewFile();
			}
			FileInputStream fis = new FileInputStream(f);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(mainJarFile);
			bis.close();
			fis.close();
			//将jar包保存本地然后删除  
			List<String> list = new ArrayList<String>();
			list.add(f.getAbsolutePath());
			String[] path = new String[]{"D:/lib/framework-pbase-2.0.0-20180328.074441-7.jar","D:/lib/service-info-api-2.0.0-SNAPSHOT.jar","D:/lib/appservice-info-api-2.0.0-SNAPSHOT.jar"};
			ModuleClassLoader.getInstance().parseJars(f.getAbsolutePath(),path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void main(String[] args) throws Exception {

	}	

	@Override
	public Result<?> getMainJarList(String mainJarName, Integer currentPage, Integer pageSize) {
		// TODO mainJarName统一命名为originalFileName
		QueryMainJar queryMainJar = new QueryMainJar();
		queryMainJar.setOriginalFileName(mainJarName);
		queryMainJar.setCurrentPage(currentPage);
		queryMainJar.setPageSize(pageSize);
		int totalNum = dubboMainJarDao.findListSize(queryMainJar);
		List<MainJar> list = dubboMainJarDao.getPagedList(queryMainJar);
		Page<MainJar> page = new Page<MainJar>(currentPage,totalNum,pageSize,list);
		
		return new Result<Page<MainJar>>("",ErrorCodeNo.SYS000,page,Status.SUCCESS);
	}

	@Override
	public Result<?> getDepJarList(String mainJarId, String originalFileName, Integer currentPage, Integer pageSize) {
		if(StringUtils.isBlank(mainJarId)){
			new Result<String>("主包id不能为空",ErrorCodeNo.SYS005,null,Status.FAILED);
		}
		QueryDepJar queryDepJar = new QueryDepJar();
		queryDepJar.setMainJarId(mainJarId);
		queryDepJar.setOriginalFileName(originalFileName);
		queryDepJar.setCurrentPage(currentPage);
		queryDepJar.setPageSize(pageSize);
		int totalNum = dubboDepJarDao.findListSize(queryDepJar);
		List<DepJar> list = dubboDepJarDao.getPagedList(queryDepJar);
		Page<DepJar> page = new Page<DepJar>(currentPage,totalNum,pageSize,list);
		
		return new Result<Page<DepJar>>("",ErrorCodeNo.SYS000,page,Status.SUCCESS);
	}

	@Override
	public Result<?> getMainJarParsedMethods(String mainJarId, String methodName, Integer currentPage,
			Integer pageSize) {
		
		return null;
	}
	
	
	private void createDownloadZip(String zipPath, String zipName, String mainJarName,
			byte[] mainJarFile, Map<String,byte[]> childrenFile,HttpServletResponse response) throws IOException{
	
		
		File zipFile = new File(zipName);
		if(!zipFile.exists()){
			zipFile.createNewFile();
		}
		
		ZipOutputStream zipOutStream = new ZipOutputStream(new FileOutputStream(zipFile));

		// TODO 优化将主包和从包放在不通的文件夹下面
		for (String key : childrenFile.keySet()) {
			zipOutStream.putNextEntry(new ZipEntry(key));
			zipOutStream.write(childrenFile.get(key));
			zipOutStream.closeEntry();
		}
		zipOutStream.close();
		this.downloadZip(zipPath, zipName, response);
	}

	private void downloadZip(String zipPath, String zipName, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("utf-8");
		File file = new File(zipPath,zipName);
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file.getPath()));
		byte[] buffer = new byte[bis.available()];
		response.reset(); //清空
		OutputStream out = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");  
        response.setHeader("Content-Disposition", "attachment;filename=" + zipName);
        out.write(buffer);
        out.flush();
        out.close();
	}

}

