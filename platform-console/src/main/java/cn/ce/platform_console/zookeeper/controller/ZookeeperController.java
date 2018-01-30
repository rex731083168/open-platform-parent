package cn.ce.platform_console.zookeeper.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.platform_service.common.ErrorCodeNo;
import cn.ce.platform_service.common.Result;
import cn.ce.platform_service.util.ZkClientUtil;
import io.netty.util.internal.StringUtil;
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

	@RequestMapping(value = "/getDubboList", method = RequestMethod.GET)
	@ApiOperation("获取接口列表")
	public Result<Map<String, String>> getDubboInfo(String parentPath) {
		Map<String, String> result = new HashMap<String, String>();
		Result<Map<String, String>> rs = new Result<Map<String, String>>();
		String[] arry = datakey.split(",");
		try {
			if (StringUtil.isNullOrEmpty(parentPath)) { //如果不传入，默认是根节点
				for (int i = 0; i < arry.length; i++) {
					result.put(arry[i], ZkClientUtil.getChildren(zkconnectioninfo, arry[i]).toString());
				}
			} else { //如果传入，就查询传入的节点
				result.put(parentPath.split("/")[1], ZkClientUtil.getChildren(zkconnectioninfo, parentPath).toString());
			}
			rs.setSuccessData(result);
		} catch (Exception e) {
			rs.setErrorCode(ErrorCodeNo.SYS001);
			rs.setErrorMessage("系统错误，接口请求失败");
		}
		return rs;
	}

}
