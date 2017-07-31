package com.salon.backstage.homepage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qc.util.MathUtil;
import com.qc.util.MathUtil.IsInt;
import com.salon.backstage.common.util.HttpRequstParmsUtil;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.common.util.Validate;
import com.salon.backstage.homepage.banner.service.IBannerService;
import com.salon.backstage.homepage.course.service.ICourseService;
import com.salon.backstage.homepage.courseChannel.service.IChannelCourseService;
import com.salon.backstage.homepage.subject.service.ISubjectService;
import com.salon.backstage.homepage.teacher.service.ITeacherService;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.Statics;

@Controller
@SuppressWarnings("all")
@RequestMapping("/homepage")
public class HomepageAction {
	@Autowired
	IBannerService bannerService;
	@Autowired
	ICourseService courseService;
	@Autowired
	ITeacherService teacherService;
	@Autowired
	IChannelCourseService channelCourseService;
	@Autowired
	ISubjectService subjectService;
	
	/**
	 * 首页-正在热播
	 */
	@RequestMapping("/playing.do")
	@ResponseBody
	public Object playing(HttpServletRequest request){
		//首页录播图信息
		List<Map> bannerList = bannerService.queryAllHomepage();
		//首页教程信息
		List<Map> courseList = courseService.queryAllHomepage();
		//首页名人大佬信息
		List<Map> teacherList =  teacherService.queryAllHomepage();
		//首页创业开店、潮流技术课程信息
		List<Map> channelCourseList = courseService.queryChannelCourseHomepage();
		Map map = new HashMap();
		map.put("bannerList", bannerList);
		map.put("courseList", courseList);
		map.put("teacherList", teacherList);
		map.put("channelCourseList", channelCourseList);
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", map));
	}
	
	
	/**
	 * 首页-即将上映
	 */
	@RequestMapping("/playingSoon.do")
	@ResponseBody
	public Object playingSoon(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "pageNo", "pagesize");
		if(!vali)
			return Send.send(Code.init(false, 1, "参数丢失！"));
		IsInt pageNo = MathUtil.isToInteger(json.get("pageNo").toString());
		IsInt pageSize = MathUtil.isToInteger(json.get("pagesize").toString());
		String userId = json.get(Send.USERID_NAME).toString();
		if(!pageNo.is||!pageSize.is||pageNo.value<1||pageSize.value<1)
			return Send.send(Code.init(false, 2, "参数不合法！"));
		Code result = courseService.getCoursesForWillPlaying(userId, pageNo.value, pageSize.value);
		return Send.send(result);
		/*
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		List<Map> coursePlayingSoonList = courseService.queryCoursePlayingSoon(json);
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", coursePlayingSoonList));
		*/
	}
	/**
	 * 点击即将上映记录次数
	 * @param request
	 * @return
	 */
	@RequestMapping("/playingSoonCount.do")
	@ResponseBody
	public Object playingSoonCount(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		String id = (String)json.get("id");
		courseService.playingSoonCount(id);
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", ""));
	}
		
	
	/**
	 * 首页-正在热播-名人大咖
	 */
	@RequestMapping("/teacherList.do")
	@ResponseBody
	public Object teacherList(HttpServletRequest request){
		List<Map> teacherAllList = teacherService.qureyAll(null,null);
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", teacherAllList));
	}
	
	
	/**
	 * 首页-正在热播-排行榜
	 */
	@RequestMapping("/courseRank.do")
	@ResponseBody
	public Object courseRank(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		int type = Integer.valueOf((String)json.get("type"));
		List<Map> rankList = null;
		if(type == 1){//热播榜
			rankList = courseService.queryPlayRank(json);
		}else if(type == 2){//热销榜
			rankList = courseService.queryOrderRank(json);
		}else {//热评榜
			rankList = courseService.queryCommentRank(json);
		}
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", rankList));
	}
	
	/**
	 * 首页-正在热播-精彩专题
	 */
	@RequestMapping("/subjectTotal.do")
	@ResponseBody
	public Object subjectTotal(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		List<Map> subjectHomepageList = subjectService.queryHomepage(json);
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", subjectHomepageList));
	}
}















