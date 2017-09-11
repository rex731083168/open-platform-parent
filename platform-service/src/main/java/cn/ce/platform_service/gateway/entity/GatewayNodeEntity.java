package cn.ce.platform_service.gateway.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author makangwei
 * 2017-8-4
 */
@Document(collection = "GW_NODE")
public class GatewayNodeEntity {

	@Id
	private String nodeId;
	@Field("nodeName")
	private String nodeName;
	@Field("nodeUrl")
	private String nodeUrl;
	@Field("nodeDesc")
	private String nodeDesc;
	@Field("colId")
	private Integer colId; //所属集群的id

	public Integer getColId() {
		return colId;
	}

	public void setColId(Integer colId) {
		this.colId = colId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeUrl() {
		return nodeUrl;
	}

	public void setNodeUrl(String nodeUrl) {
		this.nodeUrl = nodeUrl;
	}

	public String getNodeDesc() {
		return nodeDesc;
	}

	public void setNodeDesc(String nodeDesc) {
		this.nodeDesc = nodeDesc;
	}

	@Override
	public String toString() {
		return "GatewayNodeEntity [nodeId=" + nodeId + ", nodeName=" + nodeName
				+ ", nodeUrl=" + nodeUrl + ", nodeDesc=" + nodeDesc
				+ ", colId=" + colId + "]";
	}
	
	
	
}
