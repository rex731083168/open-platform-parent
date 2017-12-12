package cn.ce.platform_service.apis.entity;

/**
* @Description : 文件下载绑定api的集合
* @Author : makangwei
* @Date : 2017年12月11日
*/
// download api bound entity
public class DApiBoundList {

	private String id;
	
	private String apiChName;
	
	private String listenPath;
	
	private String apiType;
	
	private String openApplyId;
	
	private String appCode;
	
	private String appName;
	
	private ApiVersion apiVersion;
	
	private boolean isDownLoadSuccess;

	
	public DApiBoundList(String id, String apiChName, String listenPath, String apiType, String openApplyId
			, String appCode, String appName, ApiVersion apiVersion, boolean isDownLoadSuccess){
		this.id = id;
		this.apiChName = apiChName;
		this.listenPath = listenPath;
		this.apiType = apiType;
		this.openApplyId = openApplyId;
		this.appCode = appCode;
		this.appName = appName;
		this.apiVersion = apiVersion;
		this.isDownLoadSuccess = isDownLoadSuccess;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApiChName() {
		return apiChName;
	}

	public void setApiChName(String apiChName) {
		this.apiChName = apiChName;
	}

	public String getListenPath() {
		return listenPath;
	}

	public void setListenPath(String listenPath) {
		this.listenPath = listenPath;
	}

	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
	}

	public String getOpenApplyId() {
		return openApplyId;
	}

	public void setOpenApplyId(String openApplyId) {
		this.openApplyId = openApplyId;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public ApiVersion getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(ApiVersion apiVersion) {
		this.apiVersion = apiVersion;
	}

	public boolean isDownLoadSuccess() {
		return isDownLoadSuccess;
	}

	public void setDownLoadSuccess(boolean isDownLoadSuccess) {
		this.isDownLoadSuccess = isDownLoadSuccess;
	}
	
	
}
