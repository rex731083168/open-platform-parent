package cn.ce.apis.entity;

/***
 * 
 * api接口版本信息实体
 * @author lida
 * @date 2017年8月14日16:52:40
 *
 */
public class ApiVersion {

	private String apiId; //api接口id
	
	private String version ;//api版本号
	
	private String versionRemark = "第一版";//版本说明
	
	private boolean newVersion = true;//是否为当前最新版本 true为是 false为否 

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}


	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersionRemark() {
		return versionRemark;
	}

	public void setVersionRemark(String versionRemark) {
		this.versionRemark = versionRemark;
	}

	public boolean isNewVersion() {
		return newVersion;
	}

	public void setNewVersion(boolean newVersion) {
		this.newVersion = newVersion;
	}
	
	
}
