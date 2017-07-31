package com.salon.backstage.homepage.courseRecommend.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.homepage.courseRecommend.service.ICourseRecommendService;
import com.salon.backstage.qcproject.util.Send;

@Controller
@SuppressWarnings("all")
@RequestMapping("/courseRecommend")
public class CourseRecommendAction {
	
	@Autowired
	ICourseRecommendService courRecommService;
	
	/**
	 * 课程详情页-相关推荐课程的详情接口
	 */
	@RequestMapping("/courseDetailCourRecom.do")
	@ResponseBody
	public Object teacherDetailCourse(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		String courseId = (String)json.get("courseId");
		List<Map<String, Object>> courseRecomList = courRecommService.getCourseRecommend(courseId);
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", (courseRecomList==null?new ArrayList():courseRecomList)));
	}
	
	
	
}







