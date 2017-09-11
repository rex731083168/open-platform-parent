package cn.ce.platform_service.common;

/**
 *
 * @author makangwei
 * 2017-8-1
 */
public enum ErrorCodeNo {


	SYS000("系统正常")
	,SYS001("系统错误")
	,SYS003("用户未登录")
	,SYS004("返回500错误")
	;
	
	
	private String desc;
	private String codeNo;
	
	private ErrorCodeNo(String codeNo,String desc){
		this.desc = desc;
		this.codeNo = codeNo;
	}
	
	private ErrorCodeNo(String desc){
		this.desc = desc;
	}
	
	public String getDesc(){
		return desc;
	}

	public String getCodeNo() {
		return codeNo;
	}
	
}
