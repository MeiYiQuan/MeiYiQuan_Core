package com.salon.backstage.homepage.error.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.qcproject.util.Send;

@Controller
@RequestMapping("/error")
public class ErrorAction {
	
	/**
	 * 恶意登陆
	 */
	@RequestMapping("/redisNullUser.do")
	@ResponseBody
	public Object nullUser(HttpServletRequest request){
		return Send.send(MobileMessageCondition.addCondition(false, 50, "请您登录",null));
	}
	
	/**
	 * 其他账号登录
	 */
	@RequestMapping("/redisNotUniqueUser.do")
	@ResponseBody
	public Object redisNotUniqueUser(HttpServletRequest request){
		return Send.send(MobileMessageCondition.addCondition(false, 51, "您的账号已在其他设备登录",null));
	}
	
	
}












