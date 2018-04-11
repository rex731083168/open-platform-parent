package cn.ce.platform_service.dubbapply.entity;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2018年4月10日
*/
public class MainJar {

	private String id;
	
	private byte[] jarByte;

	public MainJar(){}
	
	public MainJar(String id, byte[] jarByte){
		this.id = id;
		this.jarByte = jarByte;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getJarByte() {
		return jarByte;
	}

	public void setJarByte(byte[] jarByte) {
		this.jarByte = jarByte;
	}
	
}

