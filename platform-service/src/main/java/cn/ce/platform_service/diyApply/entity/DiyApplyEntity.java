package cn.ce.platform_service.diyApply.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import cn.ce.platform_service.apis.entity.ApiAuditEntity;
import cn.ce.platform_service.users.entity.User;

/***
 * 
 * 应用实体对象
 * 
 * @author lida
 * @date 2017年8月23日14:10:18
 * @desc 用于实现应用与api之间的绑定关系
 * 
 */
@Document(collection = "DIY_APPLY")
public class DiyApplyEntity {

	@Id
	private String id; // 对应接口应用code 2， 开发者在开放平台发布应用审核

	@Field("appId")
	private String appId; // 对应接口返回应用id 2， 开发者在开放平台发布应用审核 

	@Field("applyName")
	private String applyName;// 应用名称

	/** 产品授权码 */
	@Field("productAuthCode")
	private String productAuthCode;

	/** 产品实例ID */
	@Field("productInstanceId")
	private String productInstanceId;

	/** 产品名称 */
	@Field("productName")
	private String productName;

	/** 访问域名 */
	@Field("domainUrl")
	private String domainUrl;

	/** 访问频次类型 0:标准，1:定制，2:高定制 */
	@Field("frequencyType")
	private String frequencyType;
	/** 访问频次 */
	@Field("frequencyCount")
	private String frequencyCount;

	/** 应用logo */
	@Field("logoImage")
	private byte[] logoImage;

	@Field("applyDesc")
	private String applyDesc;// 应用描述

	@Field("createDate")
	private Date createDate;// 创建时间

	@Field("userId")
	private String userId;// 创建用户id

	@Field("userName")
	private String userName;// 创建用户名

	@Field("enterpriseName")
	private String enterpriseName; // 企业名称

	/** 审核状态0:初始，1:提交审核，2:通过，3:未通过 */
	@Field("checkState")
	private Integer checkState;

	/** 审核备注 */
	@Field("checkMem")
	private String checkMem;

	@Field("authIds")
	private List<String> authIds;

	@Transient
	private List<ApiAuditEntity> auditList;// api集合

	@Transient
	private User user; // user对象

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public String getApplyDesc() {
		return applyDesc;
	}

	public void setApplyDesc(String applyDesc) {
		this.applyDesc = applyDesc;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getAuthIds() {
		return authIds;
	}

	public void setAuthIds(List<String> authIds) {
		this.authIds = authIds;
	}

	public List<ApiAuditEntity> getAuditList() {
		return auditList;
	}

	public void setAuditList(List<ApiAuditEntity> auditList) {
		this.auditList = auditList;
	}

	public String getProductAuthCode() {
		return productAuthCode;
	}

	public void setProductAuthCode(String productAuthCode) {
		this.productAuthCode = productAuthCode;
	}

	public String getDomainUrl() {
		return domainUrl;
	}

	public void setDomainUrl(String domainUrl) {
		this.domainUrl = domainUrl;
	}

	public byte[] getLogoImage() {
		return logoImage;
	}

	public void setLogoImage(byte[] logoImage) {
		this.logoImage = logoImage;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}

	public String getFrequencyCount() {
		return frequencyCount;
	}

	public void setFrequencyCount(String frequencyCount) {
		this.frequencyCount = frequencyCount;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

	public String getCheckMem() {
		return checkMem;
	}

	public void setCheckMem(String checkMem) {
		this.checkMem = checkMem;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductInstanceId() {
		return productInstanceId;
	}

	public void setProductInstanceId(String productInstanceId) {
		this.productInstanceId = productInstanceId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public List<AppList> getAppList() {
		return appList;
	}

	public void setAppList(List<AppList> appList) {
		this.appList = appList;
	}
	
	

	public List<AppList> getAppList() {
		return appList;
	}

	public void setAppList(List<AppList> appList) {
		this.appList = appList;
	}
	
	
}
