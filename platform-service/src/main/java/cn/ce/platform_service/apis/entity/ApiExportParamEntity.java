package cn.ce.platform_service.apis.entity;

import java.util.List;

/**
* @Description : 文件导出list实体封装
* @Author : makangwei
* @Date : 2017年12月11日
*/
public class ApiExportParamEntity {

	private List<String> apiIds;
	
	private List<String> appIds;
	
	public List<String> getApiIds() {
		return apiIds;
	}

	public void setApiIds(List<String> apiIds) {
		this.apiIds = apiIds;
	}

	public List<String> getAppIds() {
		return appIds;
	}

	public void setAppIds(List<String> appIds) {
		this.appIds = appIds;
	}
	
	
}
