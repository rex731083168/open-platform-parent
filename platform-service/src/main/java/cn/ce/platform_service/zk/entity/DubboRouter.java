package cn.ce.platform_service.zk.entity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年1月9日
*/
public class DubboRouter {

	private String id;
	
	private String desc;
	
	private String nodeId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public String toString() {
		return "DubboRouter [id=" + id + ", desc=" + desc + ", nodeId=" + nodeId + "]";
	}
	
}
