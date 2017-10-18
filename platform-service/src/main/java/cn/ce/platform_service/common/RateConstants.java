package cn.ce.platform_service.common;

/**
* @Description : 频次四个档位
* @Author : makangwei
* @Date : 2017年10月17日
*/
public class RateConstants {

	public static final Integer TYPE_1 = 1;
	public static final Integer TYPE_2 = 2;
	public static final Integer TYPE_3 = 3;
	public static final Integer TYPE_4 = 4;
	
	public static final Integer TYPE_1_RATE = 1;
	public static final Integer TYPE_1_PER = 100;
	public static final Integer	TYPE_1_QUOTA_MAX = 10000;
	public static final Integer TYPE_1_QUOTA_RENEW_RATE = 3600;
	
	public static final Integer TYPE_2_RATE = 1;
	public static final Integer TYPE_2_PER = 500;
	public static final Integer	TYPE_2_QUOTA_MAX = 50000;
	public static final Integer TYPE_2_QUOTA_RENEW_RATE = 3600;
	
	public static final Integer TYPE_3_RATE = 1;
	public static final Integer TYPE_3_PER = 1000;
	public static final Integer	TYPE_3_QUOTA_MAX = 100000;
	public static final Integer TYPE_3_QUOTA_RENEW_RATE = 3600;
	
//	public static final Integer TYPE_4_RATE = 1;
//	public static final Integer TYPE_4_PER = 2000;
//	public static final Integer	TYPE_4_QUOTA_MAX = 200000;
//	public static final Integer TYPE_4_QUOTA_RENEW_RATE = 3600;
}
