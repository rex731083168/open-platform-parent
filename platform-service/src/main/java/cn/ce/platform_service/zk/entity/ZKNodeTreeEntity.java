package cn.ce.platform_service.zk.entity;

import java.util.ArrayList;
import java.util.List;

public class ZKNodeTreeEntity {

	private String nodeName;

	private List<ZKNodeTreeEntity> childrednList = new ArrayList<ZKNodeTreeEntity>();

	public List<ZKNodeTreeEntity> getChildrednList() {
		return childrednList;
	}

	public void setChildrednList(List<ZKNodeTreeEntity> childrednList) {
		this.childrednList = childrednList;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}
