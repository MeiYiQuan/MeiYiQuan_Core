package com.salon.backstage.homepage.activity.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.homepage.activity.service.IActivityService;
import com.salon.backstage.qcproject.util.Send;

@Controller
@RequestMapping("/activity")
public class ActivityAction {
	@Autowired
	IActivityService activityService;
	
	/**
	 * 讲师详情页-活动的详情接口
	 * teacherId:讲师ID
	 */
	@RequestMapping("/teacherDetailActivity.do")
	@ResponseBody
	public Object teacherDetailComment(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		String teacherId = (String)json.get("teacherId");
		List<Map<String, Object>> activityList = activityService.queryByTeacherId(teacherId);
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", activityList));
	}
}














