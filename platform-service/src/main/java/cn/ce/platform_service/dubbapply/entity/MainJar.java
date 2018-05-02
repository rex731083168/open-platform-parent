package cn.ce.platform_service.dubbapply.entity;

import java.util.Date;

/**
* @Description : 
* @Author : makangwei
* @Date : 2018年4月10日
*/
public class MainJar {

	private String id; 
	
	private String originalFileName;
	
	private boolean isDeleted;
	
	private boolean isParsed;
	
	private int jarSize;
	
	private String dfsPath;
	
	private Date createTime;
	
	private Date updateTime;
	
	private String userId;
	
	private String userName;
	
	private String enterpriseName;

	public MainJar(){}
	
	public MainJar(String id, boolean isDeleted, boolean isParsed, int jarSize, String dfsPath, Date createTime,
			Date updateTime, String userId, String userName, String enterpriseName) {
		super();
		this.id = id;
		this.isDeleted = isDeleted;
		this.isParsed = isParsed;
		this.jarSize = jarSize;
		this.dfsPath = dfsPath;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.userId = userId;
		this.userName = userName;
		this.enterpriseName = enterpriseName;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isParsed() {
		return isParsed;
	}

	public void setParsed(boolean isParsed) {
		this.isParsed = isParsed;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

