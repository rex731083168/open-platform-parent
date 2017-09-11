package cn.ce.platform_service.apis.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
* @Description : api使用权限entity
* @Author : makangwei
* @Date : 2017年8月18日
*/
@Document(collection= "API_AUTH")
public class ApiAuditEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	@Field("api_id")
	private String apiId; //每个具体的api的apiid，是每个api的唯一标识
	@Field("version_api_id")
	private String versionApiId; //版本相关的apiid，版本不同，其他相同的一类api拥有相同的versionApiId;
	@Field("api_en_name")
	private String apiEnName;
	@Field("api_ch_name")
	private String apiChName;
	@Field("version")
	private String version;
	@Field("app_id")
	private String appId;
	@Field("app_key")
	private String appKey;
	@Field("app_name")
	private String appName;
	@Field("client_id")
	private String clientId;
	@Field("secret")
	private String secret;
	@Field("check_state")
	private Integer checkState; //0：未审核，1：提交审核，2：审核成功，3：审核失败，4：使用者删除调用
	@Field("user_id")
	private String userId;
	@Field("user_name")
	private String userName;
	@Field("apply_id")
	private String applyId;
	@Field("apply_name")
	private String applyName;
	@Field("supplier_id")
	private String supplier_id;
	@Field("rate")
	private Integer rate;
	@Field("per")
	private Integer per;
	@Field("quota_max")
	private Integer quotaMax;
	@Field("quota_renewal_rate")
	private Integer quotaRenewalRate;
	@Field("apply_time")
	private Date applyTime;
	@Field("check_mem")
	private String checkMem; //审核失败原因
	
	@Transient
	private APIEntity apiEntity;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getApiId() {
		return apiId;
	}
	public void setApiId(String apiId) {
		this.apiId = apiId;
	}
	public String getApiEnName() {
		return apiEnName;
	}
	public void setApiEnName(String apiEnName) {
		this.apiEnName = apiEnName;
	}
	public String getApiChName() {
		return apiChName;
	}
	public void setApiChName(String apiChName) {
		this.apiChName = apiChName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public Integer getCheckState() {
		return checkState;
	}
	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public APIEntity getApiEntity() {
		return apiEntity;
	}
	public void setApiEntity(APIEntity apiEntity) {
		this.apiEntity = apiEntity;
	}
	public String getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getRate() {
		return rate;
	}
	public void setRate(Integer rate) {
		this.rate = rate;
	}
	public Integer getPer() {
		return per;
	}
	public void setPer(Integer per) {
		this.per = per;
	}
	public Integer getQuotaMax() {
		return quotaMax;
	}
	public void setQuotaMax(Integer quotaMax) {
		this.quotaMax = quotaMax;
	}
	public Integer getQuotaRenewalRate() {
		return quotaRenewalRate;
	}
	public void setQuotaRenewalRate(Integer quotaRenewalRate) {
		this.quotaRenewalRate = quotaRenewalRate;
	}
	public String getApplyId() {
		return applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	public String getApplyName() {
		return applyName;
	}
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	public String getVersionApiId() {
		return versionApiId;
	}
	public void setVersionApiId(String versionApiId) {
		this.versionApiId = versionApiId;
	}
	public Date getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	public String getCheckMem() {
		return checkMem;
	}
	public void setCheckMem(String checkMem) {
		this.checkMem = checkMem;
	}
	
}
