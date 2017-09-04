package cn.ce.common;

/**
 *
 * @author makangwei
 * 2017-8-1
 */
public class Status {

	public static final String SUCCESS = "success";
	
	public static final String FAILED = "failed";
	
	public static final String SYSTEMERROR = "system error";
	
//	public static void successResult(Result<? extends Object> result,String msg){
//		result.setStatus(SUCCESS);
//		result.setMessage(msg);
//	}
//	
//	public static void successResultWithCode(Result<? extends Object> result,String msg,ErrorCodeNo errorNo){
//		result.setErrorCode(errorNo);
//		result.setStatus(SUCCESS);
//		result.setMessage(msg);
//	}
//	
//	public static void errorWithCode(Result<? extends Object> result,String msg,ErrorCodeNo errorNo){
//		if(msg==null){
//			errorWithCode(result,errorNo);
//			return;
//		}
//		result.setErrorCode(errorNo);
//		result.setStatus(FAILED);
//		result.setMessage(msg);
//	}
//	public static void errorWithCode(Result<? extends Object> result,ErrorCodeNo errorNo){
//		result.setErrorCode(errorNo);
//		result.setStatus(FAILED);
//	}
//	
//	public static void failedResult(Result<? extends Object> result,String msg){
//		result.setErrorCode(ErrorCodeNo.SYS1);
//		result.setStatus(FAILED);
//		result.setMessage(msg);
//	}
//	
//	public static void noUpdateResult(Result<? extends Object> result,String process){
//		result.setStatus(FAILED);
//		result.setMessage(String.format("No change occured in %s!", process));
//	}
	

}
