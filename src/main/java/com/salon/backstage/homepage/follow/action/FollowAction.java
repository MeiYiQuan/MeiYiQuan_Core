package com.salon.backstage.homepage.follow.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qc.util.Associate;
import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.common.util.NetworkUtil;
import com.salon.backstage.common.util.Validate;
import com.salon.backstage.homepage.follow.service.IFollowService;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;

/**
 * 关注表
 */
@Controller
@RequestMapping("/follow")
public class FollowAction {
	@Autowired
	IFollowService followService;
	
	/**
	 * 处理从讲师详情页面传来的关注请求
	 */
	@RequestMapping("/fromTeacherDetail.do")
	@ResponseBody
	public Object fromTeacherDetail(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		try {
			followService.changeStatusFromTeacherDetail(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", null));
		} catch (Exception e) {
			return Send.send(MobileMessageCondition.addCondition(false, 16, "关注/取消关注时发生异常", null));
		}
	}
	/**
	 * 处理从讲师详情页面传来的关注请求
	 */
	@RequestMapping("/fromFollowed.do")
	@ResponseBody
	public Object fromDetail(HttpServletRequest request){
		String ip = NetworkUtil.getIpAddr(request);
		if(ip==null)
			return Send.send(Code.init(false, 100, "未知错误！"));
		if(Associate.isDoing(ip, "follow_doing"))
			return Send.send(Code.init(false, 101, "正在执行，请稍后..."));
		
		Associate.save(ip, "follow_doing");
		
		Code result = doingSupport(request);
		
		Associate.clear(ip, "follow_doing");
		
		return Send.send(result);
	}
	
	/**
	 * 关注与取消关注的核心方法
	 * @param request
	 * @return
	 */
	private Code doingSupport(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "followedId");
		if(!vali)
			return Code.init(false, 1, "参数丢失！");
		String userId = json.get("userid").toString();
		String followId = json.get("followedId").toString();
		Code result = followService.doing(userId, followId, 3);
		return result;
	}
}










