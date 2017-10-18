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
	
	public static final String APIS_ID = "id";
	
	public static final String APIS_USERID = "userId";
	
	public static final String APIS_CHECKSTATE = "checkState";
	
	public static final String APIS_CREATE_TIME = "createTime";
	
	public static final String APIS_LISTEN_PATH = "listenPath";
	
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
	public static final String GW_API_VERSION_DATA = "version_data";
	
	public static final String GW_POLICY_ID = "id";
	public static final String GW_POLICY_ID_FULL = "policy_id";
	public static final String GW_POLICY_RATE = "rate";
	public static final String GW_POLICY_PER = "per";
	public static final String GW_POLICY_QUOTA_MAX = "quota_max";
	public static final String GW_POLICY_QUOTA_RENEWAL_RATE = "quota_renewal_rate";
	public static final String GW_POLICY_ACCESS_RIGHTS = "access_rights";
	public static final String GW_POLICY_ACCESS_RIGHTS_APIID = "api_id";
	public static final String GW_POLICY_ACCESS_RIGHTS_APINAME = "api_name";
	public static final String GW_POLICY_ACCESS_RIGHTS_VERSIONS = "versions";
	
	public static final String GW_CLIENT_ID = "client_id";
	public static final String GW_SECRET= "secret";
	public static final String GW_REDIRECT_URI = "redirect_uri";

	public static final String APIS_APPID = "appId";

}
