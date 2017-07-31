package com.salon.backstage.homepage.course.action;

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
import com.salon.backstage.homepage.comment.service.ICommentService;
import com.salon.backstage.homepage.course.service.ICourseService;
import com.salon.backstage.homepage.video.service.IVideoService;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Send;

@Controller
@RequestMapping("/course")
public class CourseAction {
	
	@Autowired
	ICourseService courseService;
	@Autowired
	IVideoService videoService;
	@Autowired
	ICommentService commentService;
	
	/**
	 * 讲师详情页-讲师的详情接口
	 */
	@RequestMapping("/teacherDetailCourse.do")
	@ResponseBody
	public Object teacherDetailCourse(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		String teacherId = (String)json.get("teacherId");
		List<Map> teacherDetailCourseList = null;
		try {
			teacherDetailCourseList = courseService.queryByTeacherId(teacherId);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", teacherDetailCourseList));
		} catch (Exception e) {
			return Send.send(MobileMessageCondition.addCondition(false, 47, "跳转讲师详情页,后台查询课程时发生异常", ""));
		}
	}
	
	/**
	 * 首页-视频详情页面
	 */
	@RequestMapping("/courseDetail.do")
	@ResponseBody
	public Object courseDetail(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		String courseId = (String)json.get("courseId");
		Map totalMap = null;
		try {
			totalMap = courseService.queryByCourseId(courseId);
			List<Map> videoList = videoService.queryByCourseId(json);
			totalMap.put("videoCount", videoList.size());
			totalMap.put("videoList", videoList);
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 48, "查询课程详情时方式异常", ""));
		}
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", totalMap));
	}
	
	/*
	首页-视频详情页面内部视频之间的跳转
	@RequestMapping("/courseDetailJumpVideo.do")
	@ResponseBody
	public Object courseDetailJumpVideo(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		String videoId = (String)json.get("videoId");
		Map totalMap = new HashMap();
		try {
			Map videoMessage = videoService.courseDetailJumpVideoMessage(json);
			// List<Map> commentMessage = commentService.queryByVideoId(videoId);
			json.put("commed_id", videoId);
			List<Map> subList=null;
			List<Map> commentMessage = commentService.getComment(json);
			if(commentMessage.size()>=2){
				 subList = commentMessage.subList(0, 2);
			}else{
				subList=commentMessage;
			}
			totalMap.put("videoMessage", videoMessage);
			totalMap.put("commentMessage", subList);
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", totalMap));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 99, "视频跳转时发生异常", ""));
		}
	}
	*/
	
	/**
	 * 频道-免费视频
	 * type:类型(1.最新2.最热3,价格)
	 */
	@RequestMapping("/channelCourse.do")
	@ResponseBody
	public Object channelCourse(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		boolean vali = Validate.validate(json, "pageNo", "pagesize", "type", "channelId");
		if(!vali)
			return Code.init(false, 1, "参数丢失！");
		IsInt type = MathUtil.isToInteger(json.get("type").toString());
		IsInt pageNo = MathUtil.isToInteger(json.get("pageNo").toString());
		IsInt pagesize = MathUtil.isToInteger(json.get("pagesize").toString());
		String channelId = json.get("channelId").toString();
		if(!pageNo.is||!type.is||!pagesize.is
				||pageNo.value<1||pagesize.value<1
				||(type.value!=1&&type.value!=2&&type.value!=3))
			return Send.send(Code.init(false, 2, "参数不合法！"));
		Code code = courseService.getChannelCourses(pageNo.value, pagesize.value, type.value, channelId);
		return Send.send(code);
		/*
		int type = Integer.valueOf((String)json.get("type"));
		List<Map> rankList = null;
		try {
			if(type == 1){ //最新
				rankList = courseService.channelTimeRank(json);
			}else if(type == 2){ //最热
				rankList = courseService.channelPlayRank(json);
			}else { //价格
				rankList = courseService.channelPriceRank(json);
			}
			return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", rankList));
		} catch (Exception e) {
			e.printStackTrace();
			return Send.send(MobileMessageCondition.addCondition(false, 96, "查询时服务器异常", ""));
		}
		*/
		
	}
}

