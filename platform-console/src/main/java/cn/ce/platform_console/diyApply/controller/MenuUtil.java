package cn.ce.platform_console.diyApply.controller;

import cn.ce.platform_service.common.gateway.ApiCallUtils;
import io.netty.handler.codec.http.HttpMethod;

/**
 * @Description : 菜单中心菜单改造为中台菜单工具类
 * @Author : makangwei
 * @Date : 2018年3月7日
 */
public class MenuUtil {

	public static void main(String[] args) {
		String url = "http://10.12.40.94:9000//menu/findMemu?tenantId=1600002102"; // 1600003169
		System.out.println(url);
		String str = ApiCallUtils.getOrDelMethod(url, null, HttpMethod.GET);

		System.out.println(str);
	}
}
