package com.salon.backstage.homepage.collect.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.homepage.collect.service.ICollectService;
import com.salon.backstage.qcproject.util.Send;

@Controller
@RequestMapping("/collect")
public class CollectAction {
	
	@Autowired
	ICollectService collectService; 

	
	/**
	 * 处理从讲师详情页面传来的收藏请求
	 */
	@RequestMapping("/fromTeacherDetail.do")
	@ResponseBody
	public Object fromTeacherDetail(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		try {
			collectService.changeStatusFromTeacherDetail(json);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", null));
		} catch (Exception e) {
			return Send.send(MobileMessageCondition.addCondition(false, 95, "收藏/取消收藏时发生异常", ""));
		}
	}
	
	/**
	 * 收藏课程
	 */
/*	@RequestMapping("/collectCourse.do")
	@ResponseBody
	public Object collectCourse(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		try {
			collectService.collecCourse(json);
			return MobileMessageCondition.addCondition(true, 0, "成功", null);
		} catch (Exception e) {
			return MobileMessageCondition.addCondition(false, 95, "收藏/取消收藏时发生异常", "");
		}
	}*/
	/**
	 * 收藏课程
	 */
	/*@RequestMapping("/collectActivity.do")
	@ResponseBody
	public Object collectActivity(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		try {
			collectService.collectActivity(json);
			return MobileMessageCondition.addCondition(true, 0, "成功", null);
		} catch (Exception e) {
			return MobileMessageCondition.addCondition(false, 95, "收藏/取消收藏时发生异常", "");
		}
	}*/
	/**
	 * 收藏
	 */
	@RequestMapping("/collect.do")
	@ResponseBody
	public Object collect(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		try {
			collectService.collect(json);//收藏
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", null));
		} catch (Exception e) {
			return Send.send(MobileMessageCondition.addCondition(false, 95, "收藏/取消收藏时发生异常", ""));
		}
	}
}
