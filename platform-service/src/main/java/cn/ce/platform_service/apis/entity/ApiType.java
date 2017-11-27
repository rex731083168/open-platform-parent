package cn.ce.platform_service.apis.entity;

/**
* @Description : 2.1期新功能，支持api可以指定不同的类型，目前有开放和保留
* @Author : makangwei
* @Date : 2017年11月14日
*/
public enum ApiType {

	OPEN("开放"),
	RETAIN("保留");
	
	private String desc;
	private String codeNo;
	
	private ApiType(String codeNo,String desc){
		this.desc = desc;
		this.codeNo = codeNo;
	}
	
	private ApiType(String desc){
		this.desc = desc;
	}
	
	public String getDesc(){
		return desc;
	}

	public String getCodeNo() {
		return codeNo;
	}
	
}
