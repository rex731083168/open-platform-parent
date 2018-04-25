package cn.ce.platform_service.dubbapply.entity;

import java.util.Date;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年4月24日
*/
public class DepJar {

	private String id; 
	
	private String mainJarId;
	
	private String originalFileName;
	
	private boolean isDeleted;
	
	private int jarSize;
	
	private String dfsPath;
	
	private Date createDate;
	
	private Date updateDate;
	
	private String userId;
	
	private String userName;
	
	private String enterpriseName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMainJarId() {
		return mainJarId;
	}

	public void setMainJarId(String mainJarId) {
		this.mainJarId = mainJarId;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getJarSize() {
		return jarSize;
	}

	public void setJarSize(int jarSize) {
		this.jarSize = jarSize;
	}

	public String getDfsPath() {
		return dfsPath;
	}

	public void setDfsPath(String dfsPath) {
		this.dfsPath = dfsPath;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	
}


