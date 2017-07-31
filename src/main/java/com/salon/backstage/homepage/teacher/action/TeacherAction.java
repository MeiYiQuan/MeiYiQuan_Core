package com.salon.backstage.homepage.teacher.action;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.salon.backstage.common.util.Paging;
import com.salon.backstage.common.util.StringUtil;
import com.salon.backstage.homepage.collect.service.ICollectService;
import com.salon.backstage.homepage.follow.service.IFollowService;
import com.salon.backstage.homepage.like.service.ILikeService;
import com.salon.backstage.homepage.statistics.service.IStatisticsService;
import com.salon.backstage.homepage.teacher.service.ITeacherService;
import com.salon.backstage.pub.bsc.dao.po.Comment;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.util.Send;
import com.salon.backstage.qcproject.util.Statics;

@Controller
@RequestMapping("/teacher")
public class TeacherAction {
	
	@Autowired
	ITeacherService teacherService;
	@Autowired
	ILikeService likeService;
	@Autowired
	IFollowService followService;
	@Autowired
	ICollectService collectService;
	@Autowired
	IStatisticsService ss;
	@Autowired
	ObjectDao od;
	
	/**
	 * 讲师详情页-讲师的详情接口
	 */
	@SuppressWarnings("all")
	@RequestMapping("/teacherDetail.do")
	@ResponseBody
	public Object teacherDetail(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		String teacherId = (String)json.get("teacherId");
		// 讲师信息
		Map teacherDetailMessage = teacherService.queryDetailAll(teacherId);
		
		if(teacherDetailMessage==null||teacherDetailMessage.size()<1)
			Send.send(MobileMessageCondition.addCondition(false, 9, "该讲师不存在！", ""));
		
		//讲师粉丝
		int teacherLikeCount = likeService.queryTeacherLikeCount(teacherId);
		//用户是否已关注讲师
		Object followStatus = followService.queryFollowOrNotByTeacherUserID(json);
		//用户是否已收藏讲师
		Object collectStatus = collectService.queryCollectOrNotByTeacherUserID(json);
		
		teacherDetailMessage.put("likeCount", teacherLikeCount);
		teacherDetailMessage.put("followStatus", followStatus);//0 未关注,1 已关注
		teacherDetailMessage.put("collectStatus", collectStatus);//0 未关注,1 已关注
		
		// 2017-01-19 添加讲师的评论量
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("commed_id", teacherId);
		int count = od.getPosCount(Comment.class, params);
		teacherDetailMessage.put("commentCount", count);
		
		ss.addStatistics(Statics.STATISTICS_CLICK_COUNT, Statics.STATICS_TYPE_JS,teacherId);
		
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", teacherDetailMessage));
	}
	
	/**
	 * 课程详情页-讲师的详情接口
	 * courseId:课程ID
	 */
	@SuppressWarnings("all")
	@RequestMapping("/courseDetailTeacher.do")
	@ResponseBody
	public Object courseDetailTeacher(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		String courseId = (String)json.get("courseId");
		Map teacherMessage = teacherService.queryCourseDetailTeacherMap(courseId);
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", teacherMessage));
	}

	/**
	 * 被求教程
	 * @param request
	 *        type:类型(1.被求教程2.讲师已上映视频3.正在投票视频4.即将上映的视频5.已录制视频)
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/teacherAskVideo.do")
	public Object teacherAskVideo(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if(StringUtil.isNullOrBlank(json.get("userid"))){
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		List<Map<String, Object>> videos=null;
		String teacherId = json.get("userid").toString();
		int page = Integer.parseInt(json.get("pageNo").toString());
		int size = Integer.parseInt(json.get("pagesize").toString());
		try {
			if(json.get("type").equals("1")){
				videos = teacherService.queryAskVideo(teacherId, page, size);//被求教程
			}else if(json.get("type").equals("2")){
				videos = teacherService.queryReleasedVideoByTeacherId(teacherId, page, size);//讲师已上映视频
			}else if(json.get("type").equals("3")){
				videos=teacherService.queryTeacherVoteVideos(teacherId, page, size);//正在投票视频
			}else if(json.get("type").equals("4")){
				videos = teacherService.queryUpcomingVideoByTeacherId(teacherId, page, size);//即将上映的视频
			}else if(json.get("type").equals("5")){
				videos = teacherService.queryTeacherRecordVideos(teacherId, page, size);//已录制视频
				//return MobileMessageCondition.addCondition(true, 0, "成功", pa);
			}
			return Send.send(MobileMessageCondition.addCondition(true, 0, "", videos));
		} catch (Exception e) {
			return Send.send(MobileMessageCondition.addCondition(false, 59, "查询讲师被请求的教程时出现异常", ""));
		}
	}

	/**
	 * 正在预定的线下活动
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/teacherAboutActivity.do")
	public Object teacherRequestVideo(HttpServletRequest request){
		Map<String, Object> json = HttpRequstParmsUtil.getParmsForMap(request);
		if(StringUtil.isNullOrBlank(json.get("userid"))){
			return Send.send(MobileMessageCondition.addCondition(false, 16, "请登录", ""));
		}
		try {
		List<Map> aboutActivitys = teacherService.queryAboutActivity(json);
		for (Map map : aboutActivitys) {
			String enrollBeginTime = map.get("enroll_begin_time").toString();//报名开始时间
			long parseLong = Long.parseLong(enrollBeginTime);
			String enrollEndTime = map.get("enroll_end_time").toString();//活动报名结束时间
			long parseLong2 = Long.parseLong(enrollEndTime);
		    Long prepareStartTime = Long.parseLong(map.get("prepare_start_time").toString());//活动准备时间
			Long prepareEndTime = Long.parseLong(map.get("prepare_end_time").toString());//活动准备结束时间
			Long activityStartTime =Long.parseLong( map.get("activity_start_time").toString());//活动开始时间
			Long activityEndTime =Long.parseLong(map.get("activity_end_time").toString());//活动结束时间
			Long cancelTime = Long.parseLong(map.get("cancel_time").toString());//活动取消时间
			Long nowDate = Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			if(nowDate>=parseLong && nowDate<=parseLong2){
				  //报名中
				  map.put("activityStatus", "报名中");
			  }else if(nowDate>=prepareStartTime && nowDate<=prepareEndTime){
				  //准备中
				  map.put("activityStatus", "准备中");
			  }else if(nowDate>=activityStartTime && nowDate<=activityEndTime){
				  //进行中
				  map.put("activityStatus", "进行中");
			  }else if(nowDate>=cancelTime){
				  //已完成
				  map.put("activityStatus", "已完成");
			  }
			  map.remove("enroll_begin_time");
			  map.remove("enroll_end_time");
			  map.remove("prepare_start_time");
			  map.remove("prepare_end_time");
			  map.remove("activity_start_time");
			  map.remove("activity_end_time");
			  map.remove("cancel_time");
			  String activityId = (String) map.get("id");//活动ID
				Long partActivityCounts = teacherService.queryCountByAcitityId(activityId);//参与活动的人数
			  map.put("partNum", partActivityCounts);
		}
		return Send.send(MobileMessageCondition.addCondition(true, 0, "成功", aboutActivitys));
		} catch (Exception e) {
			return Send.send(MobileMessageCondition.addCondition(false, 62, "查询正在预定的相关视频时出现异常", ""));
		}
	}
	
}
















