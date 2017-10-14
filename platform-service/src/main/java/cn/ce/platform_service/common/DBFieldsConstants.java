package cn.ce.platform_service.common;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年10月12日
*/
public class DBFieldsConstants {

	/*************** api相关字段 **********************/
	public static final String APIS_APIVERSION_VERSIONID = "apiVersion.versionId";
	
	public static final String APIS_APIVERSION_NEWVERSION = "apiVersion.newVersion"; 
	
	public static final String APIS_APIENNAME = "apiEnName";
	
	public static final String APIS_APICHNAME = "apiChName";
	
	public static final String APIS_OPENAPPLY_ID = "openApplyId";
	
	public static final String APIS_VERSION= "version";
	
	public static final String APIS_Id = "id";
	
	public static final String APIS_USERID = "userId";
	
	public static final String APIS_CHECKSTATE = "checkState";
	
	public static final String APIS_CREATE_TIME = "createTime";
	
	/*************** user相关字段 **********************/
	public static final String USER_USERTYPE = "userType";
	public static final String USER_USERNAME = "userName";
	public static final String USER_EMAIL = "email";
	public static final String USER_TELNUMBER = "telNumber";
	public static final String USER_ENTERPRISE_NAME = "enterpriseName";
	public static final String USER_CHECKSTATE = "checkState";
	public static final String USER_STATE = "state";
	public static final String USER_REGTIME = "regTime";

	/*************** 网关相关字段 **********************/
	public static final String GW_API_NAME = "name";
	public static final String GW_API_ID = "api_id";
	public static final String GW_API_VERSIONS = "versions";
	public static final String GW_API_VERSIONS_NAME = "name";
	public static final String GW_API_VERSIONS_EXPIRES = "expires";
	public static final String GW_API_VERSIONS_OVERRIDE_TARGET = "override_target";
	public static final String GW_API_PROXY_LISTENPATH = "listen_path";
	public static final String GW_API_PROXY = "proxy";
	
}
