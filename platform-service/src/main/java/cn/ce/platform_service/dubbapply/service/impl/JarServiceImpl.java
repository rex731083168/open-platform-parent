package cn.ce.platform_service.dubbapply.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.Status;
import cn.ce.platform_service.dubbapply.dao.IDubboDepJarDao;
import cn.ce.platform_service.dubbapply.dao.IDubboMainJarDao;
import cn.ce.platform_service.dubbapply.entity.DepJar;
import cn.ce.platform_service.dubbapply.entity.MainJar;
import cn.ce.platform_service.dubbapply.service.IJarService;
import cn.ce.platform_service.dubbapply.util.JarDfsUtil;
import cn.ce.platform_service.users.entity.User;
import cn.ce.platform_service.util.RandomUtil;

/**
* @Description : jar包上传下载service实现
* @Author : makangwei
* @Date : 2018年4月19日
*/
@Service("jarService")
public class JarServiceImpl implements IJarService{
	
	private static final Logger _LOGGER = LoggerFactory.getLogger(JarServiceImpl.class);
	
	@Resource
	private IDubboMainJarDao dubboMainJarDao;
	@Resource
	private IDubboDepJarDao dubboDepJarDao;
	
	@Override
	public Result<?> uploadMainJar(MultipartFile mainJar, User user) {
		
		// TODO 先保存到fastdfs
		String fileName = mainJar.getOriginalFilename();
		byte[] bytes;
		try {
			bytes = mainJar.getBytes();
		} catch (IOException e1) {
			_LOGGER.error("获取文件内容异常，堆栈信息如下：");
			e1.printStackTrace();
			return new Result<String>("jar包内容异常",ErrorCodeNo.UPLOAD003,null,Status.FAILED);
		}
		
		String jarPath = null;
		try {
			jarPath = JarDfsUtil.getInstance().saveFile(fileName, bytes);
		} catch (Exception e1) {
			_LOGGER.error("fastdfs客户端连接异常，堆栈信息如下：");
			e1.printStackTrace();
			return new Result<String>("fastdfs客户端连接错误",ErrorCodeNo.UPLOAD004, null,Status.FAILED);
		}
		if(StringUtils.isBlank(jarPath)){
			//插入fastdfs失败
			return new Result<String>("上传失败，请稍后再试",ErrorCodeNo.UPLOAD005,null,Status.FAILED);
		}
		 
		//更新数据库
		MainJar jar = new MainJar();
		
		jar.setId(RandomUtil.random32UUID());
		jar.setCreateDate(new Date());
		jar.setUpdateDate(jar.getCreateDate()); 
		jar.setDeleted(false);
		jar.setDfsPath(jarPath);
		jar.setJarSize(bytes.length);
		jar.setEnterpriseName(user.getEnterpriseName());
		jar.setUserId(user.getId());
		jar.setUserName(user.getUserName());
		jar.setParsed(false);
		jar.setOriginalFileName(fileName);
		
		@SuppressWarnings("unused")
		int i = dubboMainJarDao.saveMainJar(jar);
		
		return new Result<String>("主包上传成功",ErrorCodeNo.SYS000,null,Status.SUCCESS);
	}
	
	@Override
	public Result<?> updateMainJar(String mainJarId, MultipartFile mainJar) {
		
		MainJar main = dubboMainJarDao.findMainJarById(mainJarId);
		if(null == main || null == main.getOriginalFileName()){
			return new Result<String>("当前id不存在",ErrorCodeNo.SYS006,null,Status.FAILED);
		}
		String dfsPath = main.getDfsPath();
		String newFileName = mainJar.getOriginalFilename();
		byte[] newJarBytes;
		try {
			newJarBytes = mainJar.getBytes();
		} catch (IOException e1) {
			_LOGGER.error("获取文件内容异常，堆栈信息如下：");
			e1.printStackTrace();
			return new Result<String>("jar包内容异常",ErrorCodeNo.UPLOAD003,null,Status.FAILED);
		}
		String newPath = null;
		try {
			newPath = JarDfsUtil.getInstance().updateFile(dfsPath, newFileName, newJarBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result<String>("获取fastdfs客户端失败",ErrorCodeNo.UPLOAD004,null,Status.FAILED);
		}
		if(StringUtils.isBlank(newPath)){
			return new Result<String>("跟新失败",ErrorCodeNo.UPLOAD006,null,Status.FAILED);
		}
		
		main.setOriginalFileName(newFileName);
		main.setDfsPath(newPath);
		main.setUpdateDate(new Date());
		dubboMainJarDao.updateMainJar(main);
		return new Result<String>("跟新成功",ErrorCodeNo.SYS000,null,Status.SUCCESS);
	}

	@Override
	public Result<?> deletedMainJar(String mainJarId) {
		
		MainJar main =  dubboMainJarDao.findMainJarById(mainJarId);
		if(null ==main || StringUtils.isBlank(main.getDfsPath())){
			return new Result<String>("当前id不存在",ErrorCodeNo.SYS006,null,Status.FAILED);
		}
		String dfsPath = main.getDfsPath();
		
		boolean bool = false;
		try {
			bool = JarDfsUtil.getInstance().deleteFile(dfsPath);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result<String>("删除失败",ErrorCodeNo.UPLOAD007,null,Status.FAILED);
		}
		if(!bool){
			return new Result<String>("删除失败",ErrorCodeNo.UPLOAD007,null,Status.FAILED);
		}
		main.setDeleted(true);
		dubboMainJarDao.updateMainJar(main);
		return new Result<String>("删除成功",ErrorCodeNo.SYS000,null,Status.SUCCESS);
	}
	
	@Override
	public Result<?> uploadDepJar(String mainJarId, MultipartFile[] depJar, User user) {
		
		// 上传到fastdfs。保存到数据库中
		MainJar main = dubboMainJarDao.findMainJarById(mainJarId);
		if(null == main || StringUtils.isBlank(main.getDfsPath())){
			return new Result<String>("当前id不存在",ErrorCodeNo.SYS006,null,Status.FAILED);
		}
		
		Map<String,byte[]> jarMap = new HashMap<String,byte[]>();
		
		for (MultipartFile multipartFile : depJar) {
			String fileName = multipartFile.getOriginalFilename();
			byte[] bs = null;
			try {
				bs = multipartFile.getBytes();
			} catch (IOException e) {
				_LOGGER.error("表单文件解析异常。异常信息如下：");
				e.printStackTrace();
				return new Result<String>("文件解析异常",ErrorCodeNo.UPLOAD003,null,Status.FAILED);
			}
			jarMap.put(fileName, bs);
		}
		
		//发成异常时回滚的集合
		Map<String,String> rollDfsPaths = new HashMap<String,String>();
		
		JarDfsUtil dfsUtil = null;
		try {
			dfsUtil = JarDfsUtil.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return new Result<String>("文件解析异常",ErrorCodeNo.UPLOAD004,null,Status.FAILED);
		}
		
		for (String fileName : jarMap.keySet()) {
			String result = dfsUtil.saveFile(fileName, jarMap.get(fileName));
			if(StringUtils.isBlank(result)){
				rollbackDfsCreate(rollDfsPaths);
				return new Result<String>("fastdfs客户端连接错误",ErrorCodeNo.UPLOAD004, null,Status.FAILED);
			}
			rollDfsPaths.put(fileName,result);
		}
		
		//全部插入fastdfs后插入数据库
		for (String fileName: rollDfsPaths.keySet()) {
			DepJar dep = new DepJar();
			dep.setId(RandomUtil.random32UUID());
			dep.setMainJarId(main.getId());
			dep.setCreateDate(new Date());
			dep.setUpdateDate(dep.getCreateDate());
			dep.setDfsPath(rollDfsPaths.get(fileName));
			dep.setDeleted(false);
			dep.setJarSize(jarMap.get(fileName).length);
			dep.setOriginalFileName(fileName);
			dep.setEnterpriseName(user.getEnterpriseName());
			dep.setUserId(user.getId());
			dep.setUserName(user.getUserName());
			dubboDepJarDao.saveDepJar(dep);
		}
		
		return new Result<String>("依赖包上传成功,上传的依赖包的数量是："+rollDfsPaths.size(),ErrorCodeNo.SYS000,null,Status.SUCCESS);
	}

	private boolean rollbackDfsCreate(Map<String,String> rollDfsPaths) {
		JarDfsUtil dfsUtil = null;
		try {
			dfsUtil = JarDfsUtil.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		for (String string : rollDfsPaths.keySet()) {
			try {
				dfsUtil.deleteFile(rollDfsPaths.get(string));
			} catch (Exception e) {
			}
		}
		return true;
	}

	@Override
	public Result<?> updateSingleDepJar(String depJarId, MultipartFile depJar) {
		DepJar dep = dubboDepJarDao.findDepJarById(depJarId);
		if(null == dep || null == dep.getOriginalFileName()){
			return new Result<String>("当前id不存在",ErrorCodeNo.SYS006,null,Status.FAILED);
		}
		String dfsPath = dep.getDfsPath();
		String newFileName = depJar.getOriginalFilename();
		byte[] newJarBytes;
		try {
			newJarBytes = depJar.getBytes();
		} catch (IOException e1) {
			_LOGGER.error("获取文件内容异常，堆栈信息如下：");
			e1.printStackTrace();
			return new Result<String>("jar包内容异常",ErrorCodeNo.UPLOAD003,null,Status.FAILED);
		}
		String newPath = null;
		try {
			newPath = JarDfsUtil.getInstance().updateFile(dfsPath, newFileName, newJarBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result<String>("获取fastdfs客户端失败",ErrorCodeNo.UPLOAD004,null,Status.FAILED);
		}
		if(StringUtils.isBlank(newPath)){
			return new Result<String>("跟新失败",ErrorCodeNo.UPLOAD006,null,Status.FAILED);
		}
		
		dep.setOriginalFileName(newFileName);
		dep.setDfsPath(newPath);
		dep.setUpdateDate(new Date());
		dubboDepJarDao.updateDepJar(dep);
		return new Result<String>("跟新成功",ErrorCodeNo.SYS000,null,Status.SUCCESS);
	}

	@Override
	public Result<?> deleteSingleDepJar(String depJarId) {
		
		DepJar dep =  dubboDepJarDao.findDepJarById(depJarId);
		if(null ==dep || StringUtils.isBlank(dep.getDfsPath())){
			return new Result<String>("当前id不存在",ErrorCodeNo.SYS006,null,Status.FAILED);
		}
		String dfsPath = dep.getDfsPath();
		
		boolean bool = false;
		try {
			bool = JarDfsUtil.getInstance().deleteFile(dfsPath);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result<String>("删除失败",ErrorCodeNo.UPLOAD007,null,Status.FAILED);
		}
		if(!bool){
			return new Result<String>("删除失败",ErrorCodeNo.UPLOAD007,null,Status.FAILED);
		}
		dep.setDeleted(true);
		dubboDepJarDao.updateDepJar(dep);
		return new Result<String>("删除成功",ErrorCodeNo.SYS000,null,Status.SUCCESS);
	}

	@Override
	public Result<?> deleteDepJars(List<String> depJarIds) {
		
		List<DepJar> deps =  dubboDepJarDao.findDepJarByIds(depJarIds);
		if(null == deps || deps.size() == 0){
			return new Result<String>("当前id查询不到数据",ErrorCodeNo.SYS006,null,Status.FAILED);
		}
		
		int successNum= 0;
		for (DepJar dep : deps) {
			String dfsPath = dep.getDfsPath();
			boolean bool = false;
			try {
				bool = JarDfsUtil.getInstance().deleteFile(dfsPath);
			} catch (Exception e) {
				e.printStackTrace();
				//发生异常继续执行下一条数据
				continue;
			}
			if(bool){
				dep.setDeleted(true);
				dubboDepJarDao.updateDepJar(dep);
				successNum++;
			}
		}
		
		return new Result<String>("总量是"+deps.size()+",删除成功"+successNum+"条",ErrorCodeNo.SYS000,null,Status.SUCCESS);
	}

}

