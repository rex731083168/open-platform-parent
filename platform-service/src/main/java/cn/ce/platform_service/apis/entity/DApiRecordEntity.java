package cn.ce.platform_service.apis.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年12月12日
*/
@Document(collection="RECORD_DOWNAPI")
public class DApiRecordEntity {

	@Id
	private String id;
	@Field("apiIds")
	List<String> apiIds;//绑定列表
	@Field("operaTime")
	private Date operaTime; //操作时间
	@Field("totalNum")
	private int totalNum;
	@Field("operaUserName")
	private String operaUserName; //操作人的用户名
	@Field("userId")
	private String userId;
	
	public DApiRecordEntity(List<String> apiIds, Date operaTime, int totalNum, String operaUserName, String userId){
		this.apiIds = apiIds;
		this.operaTime = operaTime;
		this.totalNum = totalNum;
		this.operaUserName = operaUserName;
		this.userId = userId;
	}
	
	public DApiRecordEntity(Date operaTime, int totalNum, String operaUserName, String userId){
		this.operaTime = operaTime;
		this.totalNum = totalNum;
		this.operaUserName = operaUserName;
		this.userId = userId;
	}
	
	public DApiRecordEntity(){
		super();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getApiIds() {
		return apiIds;
	}
	public void setApiIds(List<String> apiIds) {
		this.apiIds = apiIds;
	}
	public Date getOperaTime() {
		return operaTime;
	}

	public void setOperaTime(Date operaTime) {
		this.operaTime = operaTime;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public String getOperaUserName() {
		return operaUserName;
	}
	public void setOperaUserName(String operaUserName) {
		this.operaUserName = operaUserName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
