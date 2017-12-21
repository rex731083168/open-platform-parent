package cn.ce.platform_console.zookeeper.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.common.cachelocal.CacheManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Component
@RestController
@RequestMapping("/dubboInfo")
@Api("dubbo接口信息")
public class ZookeeperController {

	@Value("#{redis['zookeeper.connection']}")
	private String zkconnectioninfo;
	@Value("#{redis['dubbo.node']}")
	private String datakey;

	@Autowired
	private CacheManager cacheManager;
	@Resource
	private UpdateData updateData;

	@RequestMapping(value = "/getDubboList", method = RequestMethod.GET)
	@ApiOperation("获取接口列表")
	public Result<Map<String, String>> getDubboInfo() {
		Map<String, String> result = new HashMap<String, String>();
		Result rs = new Result<>();
		String[] arry = datakey.split(",");
		for (int i = 0; i < arry.length; i++) {
			if (!cacheManager.hasCache(arry[i])) {
				updateData.UpdateData(zkconnectioninfo, datakey);
			}
			result.put(arry[i], cacheManager.getCache(arry[i]).toString());
		}
		rs.setData(result);
		return rs;
	}

}
