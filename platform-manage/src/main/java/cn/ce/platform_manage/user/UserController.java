package cn.ce.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.ce.common.Result;
import cn.ce.mail.MailInfo;
import cn.ce.mail.MailUtil;
import cn.ce.page.Page;
import cn.ce.users.entity.User;
import cn.ce.users.service.IUserService;

/**
* @Description : 说明
* @Author : makangwei
* @Date : 2017年8月22日
*/
@RestController
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private IUserService userService;
	
	//后台获取使用者列表
	@RequestMapping(value = "/approveUsers", method = RequestMethod.POST)
	@ResponseBody
	public String approveUsers(

			HttpServletRequest request,
			HttpServletResponse response,
			String roleType,
			String uname,
			String checkstate,
			String state,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam(required = false, defaultValue = "8") int pageSize) {

		// TODO 解决异常处理
		int role = Integer.parseInt(roleType.trim());

		JSONObject result = new JSONObject();
		try {
			Page<User> userList = userService.getUsers(role, uname, checkstate,state,currentPage, pageSize);
			List<User> items = (List<User>) userList.getItems();
			JSONArray jsonArray = new JSONArray();
			JSONObject data = new JSONObject();
			for (int i = 0; i < items.size(); i++) {
				JSONObject obj = new JSONObject();
				User user = items.get(i);
				obj.put("id", user.getId());
				obj.put("username", user.getUsername());
				obj.put("email", user.getEmail());
				obj.put("tel", user.getTel());
				obj.put("regtime", user.getRegtime());
				obj.put("checkmem", user.getCheckMem());
				obj.put("checkstate", user.getCheckState());
				obj.put("state", user.getState());
				jsonArray.put(obj);
			}
			data.put("items", jsonArray);
			data.put("pageSize", userList.getPageSize());
			data.put("currentPage", userList.getCurrentPage());
			data.put("totalNumber", userList.getTotalNumber());
			data.put("totalPage", userList.getTotalPage());
			result.put("data", data);
			result.put("code", "1");
			result.put("message", "OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
	@RequestMapping(value="deleteUserById",method=RequestMethod.GET)
	public Result<String> deleteUserById(HttpServletRequest request,HttpServletResponse response,
			String userId){
		
		return userService.deleteById(userId);
	}
	
	/**
	 * @Description : 管理员后台可以重置用户密码，需要传入加密后的密码
	 * @Author : makangwei
	 * @Date : 2017年8月23日
	 */
	@RequestMapping(value="/resetUserPassword",method=RequestMethod.POST)
	@ResponseBody
	public Result<String> resetUserPassword(HttpServletRequest request, HttpServletResponse response,
			String userId,String newPassword){
		
		return userService.resetUserPassword(userId,newPassword);
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.POST)
	@ResponseBody
	public String approve(
			HttpServletRequest request,
			HttpServletResponse response,
			String id,
			String checkmem,
			String checkstate) {

		id = id.trim();
		JSONObject result = new JSONObject();
		
		try {
			
			User user = userService.findOne(id);
			user.setCheckState(Integer.valueOf(checkstate));
			user.setCheckMem(checkmem);

			int i = userService.modifyById(id, user);
			
			
			logger.info("------->邮件通知用户，帐号审核结果");
			if(i > 0 && "2".equals(checkstate)) {
				MailInfo mailInfo = new MailInfo();
				mailInfo.setContent("您申请注册的开放平台帐号已经审核通过。欢迎使用中企云Pass开放平台！");
				mailInfo.setSubject("中企云Pass开放平台账户审核结果通知");
				mailInfo.setToOne(user.getEmail());
				MailUtil.send(mailInfo, false);
			}else if(i > 0 && "3".equals(checkstate)){
				MailInfo mailInfo = new MailInfo();
				mailInfo.setContent("很抱歉通知您，您申请的开放平台账户未审核通过，原因是："+checkmem+",请重新申请");
				mailInfo.setSubject("中企云Pass开放平台账户审核结果通知");
				mailInfo.setToOne(user.getEmail());
				MailUtil.send(mailInfo, false);
			}

			result.put("code", "1");
			result.put("message", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "0");
			result.put("message", "ERROR");
		}
		return result.toString();
	}
	
	@RequestMapping(value = "/forbid", method = RequestMethod.POST)
	@ResponseBody
	public String forbid(

			HttpServletRequest request,
			HttpServletResponse response,
			String userid,
			String state) {

		userid = userid.trim();
		JSONObject result = new JSONObject();
		
		try {
			
			int st = Integer.parseInt(state);
			User user = userService.findOne(userid);
			user.setState(st);
			userService.modifyById(userid, user);
			result.put("code", "1");
			result.put("message", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "0");
			result.put("message", "ERROR");
		}
		return result.toString();
	}


}
