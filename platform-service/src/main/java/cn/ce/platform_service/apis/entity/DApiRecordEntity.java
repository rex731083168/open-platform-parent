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
	@Field("operaDate")
	private Date operaDate; //操作时间
	@Field("totalNum")
	private int totalNum;
	@Field("operaUserName")
	private String operaUserName; //操作人的用户名
	@Field("userId")
	private String userId;
	
	public DApiRecordEntity(List<String> apiIds, Date operaDate, int totalNum, String operaUserName, String userId){
		this.apiIds = apiIds;
		this.operaDate = operaDate;
		this.totalNum = totalNum;
		this.operaUserName = operaUserName;
		this.userId = userId;
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
	public Date getOperaDate() {
		return operaDate;
	}
	public void setOperaDate(Date operaDate) {
		this.operaDate = operaDate;
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
