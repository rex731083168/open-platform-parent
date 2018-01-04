package cn.ce.platform_service.gateway.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author makangwei
 * 2017-8-4
 */
@Document(collection = "GW_COLONY")
public class GatewayColonyEntity {

	@Id 
	private String colId; //集群id
	@Field("colName") 
	private String colName;	//集群名称	
	@Field("colUrl")
	private String colUrl;	//集群代理域名(内网)
	@Field("wColUrl")
	private String wColUrl; //集群代理域名(外网)
	@Field("colStatus")
	private Integer colStatus;	//集群状态 1可用，0禁用
	@Field("colDesc")
	private String colDesc;  //集群描述
	
	
	public String getwColUrl() {
		return wColUrl;
	}

	public void setwColUrl(String wColUrl) {
		this.wColUrl = wColUrl;
	}

	public Integer getColStatus() {
		return colStatus;
	}

	public String getColId() {
		return colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
	}

	public void setColStatus(Integer colStatus) {
		this.colStatus = colStatus;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getColUrl() {
		return colUrl;
	}
	public void setColUrl(String colUrl) {
		this.colUrl = colUrl;
	}

	public String getColDesc() {
		return colDesc;
	}
	public void setColDesc(String colDesc) {
		this.colDesc = colDesc;
	}
	@Override
	public String toString() {
		return "GatewayColonyEntity [colId=" + colId + ", colName=" + colName
				+ ", colUrl=" + colUrl + ", colStatus=" + colStatus
				+ ", colDesc=" + colDesc + "]";
	}
}
