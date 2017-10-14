package cn.ce.platform_service.diyApply.entity.inparameter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @Title: SaveOrUpdateAppsInParameterEntity.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年10月14日 time下午4:04:02
 *
 **/
public class SaveOrUpdateAppsInParameterEntity {

	private String appId;// 应用appId appId为空则为新增, appId不为空 更新app
	private String appName;// 应用名称 必填
	private String appUrl;// 应用URL 选填
	private String appDesc;// 应用描述 选填
	private String appCatogroyId;// 应用分类ID 选填，默认注册到定制的分类下
	private String appTag;// 模块标识 选填 模块标识
	private String appCode;// app code 必填 此参数为
	private String appType;// 必填 1 标品,2
	private String owner;// 所属企业标记 必填 中企动力默认为 CE

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getAppDesc() {
		return appDesc;
	}

	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}

	public String getAppCatogroyId() {
		return appCatogroyId;
	}

	public void setAppCatogroyId(String appCatogroyId) {
		this.appCatogroyId = appCatogroyId;
	}

	public String getAppTag() {
		return appTag;
	}

	public void setAppTag(String appTag) {
		this.appTag = appTag;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

}
