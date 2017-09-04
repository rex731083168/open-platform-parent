package cn.ce.common;

/**
 * 
 * @ClassName: Constants
 * @Description: 通用常量定义
 * @author dingjia@300.cn
 *
 */
public class Constants {

//	------------------------网关相关-----------------------------------------------------
	public static final String HEADER_KEY = "X-Tyk-Authorization";

	public static final String HEADER_VALUE = "352d20ee67be67f6340b4c0605b044b7";
	
	public static final String NETWORK_CREATE_CLIENT = "/tyk/oauth/clients/create";

	public static String NETWORK_AUTHORIZE_CLIENT = "/${listen_path}/tyk/oauth/authorize-client/";
	
	public static final String RESP_HEADER_CRE_VALUE = "true";

	public static final String NETWORK_RELOAD_PATH = "/tyk/reload/";

	public static final String NETWORK_ADD_PATH = "/tyk/apis";

	public static final String NETWORK_DEL_PATH = "/tyk/apis";

	public static final String NETWORK_UPDATE_PATH = "/tyk/apis";

	public static final String NETWORK_GET_PATH = "/tyk/apis";

	public static final String NETWORK_ADD_KEY_PATH = "/tyk/keys";

	public static final String NETWORK_DEL_KEY_PATH = "/tyk/keys";
	
	
//	-----------------------HTTP请求相关-------------------------------------------------
	public static final String HEADER_CONTENT_TYPE = "Content-type";

	public static final String HEADER_FORM_REQUEST = "application/x-www-form-urlencoded";

	public static final String APPLICATION_JSON = "application/json";
	
	public static final String ADD = "ADD";

	public static final String DELETE = "DELETE";

	public static final String UPDATE = "UPDATE";

	public static final String SELECT = "SELECT";

	public static final String RELOAD = "RELOAD";
	

//	-----------------------返回状态码相关-------------------------------------------------	
	public static final String RESULT_ERROR = "ERROR";

	public static final String RESULT_OK = "OK";

	public static final String SUCCESS = "1";

	public static final String FAILER = "0";
	

//	-----------------------表结构相关-------------------------------------------------	
	/** 表名称-用户表 */
	public static final String COL_APIMG_USER = "APIMG_USERS";
	/** 表名称-用户表 */
	public static final String COL_APIMG_ADMIN = "APIMG_ADMIN";
	/** 表名称-公共参数表 */
	public static final String COL_APIMG_API_SYSARG = "APIMG_API_SYSARG";
	/** 表名称-服务分类表 */
	public static final String COL_APIMG_APPS = "APIMG_APPS";
	/** 表名称-接口表 */
	public static final String COL_APIMG_APIS = "APIMG_APIS";
	/** 表名称-配置表 */
	public static final String COL_APIMG_SYS_CONFIG = "APIMG_SYS_CONFIG";
	/** 表名称-黑名单 */
	public static final String COL_APIMG_BLACK = "APIMG_BLACK";
	/** 表名称-接口调用日志 */
	public static final String COL_APIMG_STATS = "APIMG_STATS";
	

//	-----------------------业务参数相关-------------------------------------------------	
	/** 排序--升序 */
	public static final int SORT_TYPE_ASC = 1;
	/** 排序--降序 */
	public static final int SORT_TYPE_DESC = 2;
	/** 登录类型-普通用户 */
	public static final int USER_ROLE_CONSUMER = 1;
	/** 登录类型-提供者 */
	public static final int USER_ROLE_SUPPLIER = 2;
	/** 登录类型-管理员 */
	public static final int USER_ROLE_ADMIN = 0;
	/** 登录用户 */
	public static final String SES_LOGIN_USER = "loginuser";
	/** 加密长度 */
	public static final int SECRET_LENGTH = 32;
	/** 分页长度 */
	public static final int PAGE_SIZE = 8;
}
