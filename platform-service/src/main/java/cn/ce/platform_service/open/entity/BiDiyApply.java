package cn.ce.platform_service.open.entity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年6月8日
*/
public class BiDiyApply {

	private String id;
	
	private String appId;
	
	private String applyName;
	
	private String clientId;
	
	private String applyDesc;
	
	private String userId;
	
	private Integer checkState;
	
	private Long count;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getApplyDesc() {
		return applyDesc;
	}

	public void setApplyDesc(String applyDesc) {
		this.applyDesc = applyDesc;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "BiDiyApply [id=" + id + ", appId=" + appId + ", applyName=" + applyName + ", clientId=" + clientId
				+ ", applyDesc=" + applyDesc + ", userId=" + userId + ", checkState=" + checkState + ", count=" + count
				+ "]";
	}
}

