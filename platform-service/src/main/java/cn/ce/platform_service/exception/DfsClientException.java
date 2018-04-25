package cn.ce.platform_service.exception;

/**
* @Description : fastdfs 客户端异常
* @Author : makangwei
* @Date : 2018年4月20日
*/
public class DfsClientException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public DfsClientException(){
        super();
    }
	
    public DfsClientException(String msg){
        super(msg);
    }
}

