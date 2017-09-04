package cn.ce.common;

/**
 *
 * @author makangwei
 * 2017-8-1
 */
public class Result <T> {

	private T data;
	
	private String status = Status.FAILED;
	
	private String message;
	
	private ErrorCodeNo errorCode = ErrorCodeNo.SYS000;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setErrorMessage(String message){
		this.message = message;
		this.status = Status.FAILED;
	}
	
	public void setSuccessMessage(String message){
		this.message = message;
		this.status = Status.SUCCESS;
	}
	
	public void setSuccessData(T data){
		this.data = data;
		this.status = Status.SUCCESS;
	}

	public ErrorCodeNo getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCodeNo errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "Result [data=" + data + ", status=" + status + ", message="
				+ message + ", errorCode=" + errorCode + "]";
	}
	
	
}
