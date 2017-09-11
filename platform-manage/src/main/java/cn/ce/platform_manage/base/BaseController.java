package cn.ce.platform_manage.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
	 /** 请求对象 */
	    @Autowired  
	    public   HttpServletRequest request; 
	
	    /** 会话对象 */
	    @Autowired
	    public  HttpSession session ;
}

