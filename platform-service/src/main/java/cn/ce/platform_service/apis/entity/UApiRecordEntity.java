package cn.ce.platform_service.apis.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
* @Description : 文件下载绑定的记录实体
* @Author : makangwei
* @Date : 2017年12月11日
*/
@Document(collection="RECORD_UPLOADAPI")
public class UApiRecordEntity {

	@Id
	private String id;
	@Field("uApiRecordList")
	List<UApiRecordList> uApiRecordList;//绑定列表
	@Field("operaTime")
	private Date operaTime; //操作时间
	@Field("totalNum")
	private int totalNum;
	@Field("successNum")
	private int successNum;
	@Field("operaUserName")
	private String operaUserName; //操作人的用户名
	@Field("operaUserId")
	private String operaUserId;
	
	public UApiRecordEntity(List<UApiRecordList> uApiRecordList, Date operaTime
			, int totalNum, int successNum, String operaUserName, String operaUserId){
		this.uApiRecordList = uApiRecordList;
		this.operaTime = operaTime;
		this.totalNum = totalNum;
		this.successNum = successNum;
		this.operaUserName = operaUserName;
		this.operaUserId = operaUserId;
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

	public int getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(int successNum) {
		this.successNum = successNum;
	}

	public String getOperaUserName() {
		return operaUserName;
	}

	public void setOperaUserName(String operaUserName) {
		this.operaUserName = operaUserName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<UApiRecordList> getuApiRecordList() {
		return uApiRecordList;
	}

	public void setuApiRecordList(List<UApiRecordList> uApiRecordList) {
		this.uApiRecordList = uApiRecordList;
	}

	public String getOperaUserId() {
		return operaUserId;
	}

	public void setOperaUserId(String operaUserId) {
		this.operaUserId = operaUserId;
	}
	
}
