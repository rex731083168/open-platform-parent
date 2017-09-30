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
	,SYS004("程序发生未处理的异常")
	,SYS005("部分参数为空")
	,SYS006("当前查询结果不存在")
	,SYS007("当前分组内容不为空")
	,SYS008("部分参数错误")
	,SYS009("已存在")
	,SYS010("不可重复")
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
