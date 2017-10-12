package cn.ce.platform_service.common;

/**
* @Description : 状态相关的常量
* @Author : makangwei
* @Date : 2017年10月11日
*/
public class AuditConstants {

	/********** 提供者 提供的开发应用审核状态  **********/
	public static final int OPEN_APPLY_UNCHECKED = 0;	//未审核
	public static final int OPEN_APPLY_CHECKED_COMMITED = 1;	//待审核
	public static final int OPEN_APPLY_CHECKED_SUCCESS = 2;	//审核成功
	public static final int OPEN_APPLY_CHECKED_FAILED = 3;	//审核失败
	
	/********** 用户状态  **********/
	public static final int USER__UNCHECKED = 0;	//未审核
	public static final int USER__COMMITED = 1;	//待审核
	public static final int USER__CHECKED_SUCCESS = 2;	//审核成功
	public static final int USER__CHECKED_FAILED =3;	//审核失败
	
	/********** 用户角色  **********/
	public static final int USER_ADMINISTRATOR = 0; //管理员
	public static final int USER_DEVELOPER= 1; //开发者
	public static final int USER_PROVIDER = 2;	//提供者
	
	
}
