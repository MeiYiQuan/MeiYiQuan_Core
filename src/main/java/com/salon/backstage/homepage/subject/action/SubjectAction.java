package com.salon.backstage.homepage.subject.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.homepage.course.service.ICourseService;
import com.salon.backstage.homepage.subject.service.ISubjectService;
import com.salon.backstage.qcproject.util.Send;

@Controller
@RequestMapping("/subject")
public class SubjectAction {
	
	@Autowired
	ISubjectService subjectService;
	@Autowired
	ICourseService courseService;
	
	/**
	 * 首页-精彩专题-其中一个专题信息
	 */
	@SuppressWarnings("all")
	@RequestMapping("/singleSubject.do")
	@ResponseBody
	public Object singleSubject(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		// 查询[首页-精彩专题-其中一个专题信息]中对应的课程信息
		List<Map> courseMessage = courseService.queryBySubject(json);
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", courseMessage));
	}
	
}









