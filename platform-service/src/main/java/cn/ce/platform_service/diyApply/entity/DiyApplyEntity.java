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
	private String id;

	@Field("applyName")
	private String applyName;// 应用名称

	/** 产品授权码 */
	@Field("productAuthCode")
	private String productAuthCode;
	/** 访问域名 */
	@Field("domainUrl")
	private String domainUrl;
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

}
