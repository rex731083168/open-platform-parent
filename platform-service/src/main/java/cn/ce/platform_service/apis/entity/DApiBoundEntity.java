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
@Document(collection="RECORD_DOWNAPI")
public class DApiBoundEntity {

	@Id
	private String id;
	@Field("dApiBoundList")
	List<DApiBoundList> dApiBoundList;//绑定列表
	@Field("operaDate")
	private Date operaDate; //操作时间
	@Field("totalNum")
	private int totalNum;
	@Field("successNum")
	private int successNum;
	@Field("operaUserName")
	private String operaUserName; //操作人的用户名
	
	
	public DApiBoundEntity(List<DApiBoundList> dApiBoundList, Date operaDate, int totalNum, int successNum, String operaUserName){
		this.dApiBoundList = dApiBoundList;
		this.operaDate = operaDate;
		this.totalNum = totalNum;
		this.successNum = successNum;
		this.operaUserName = operaUserName;
	}
	
	public List<DApiBoundList> getdApiBoundList() {
		return dApiBoundList;
	}

	public void setdApiBoundList(List<DApiBoundList> dApiBoundList) {
		this.dApiBoundList = dApiBoundList;
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
	
	
}
