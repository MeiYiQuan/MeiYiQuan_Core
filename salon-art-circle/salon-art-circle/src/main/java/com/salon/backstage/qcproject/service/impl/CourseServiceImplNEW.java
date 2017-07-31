package com.salon.backstage.qcproject.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.homepage.statistics.service.IStatisticsService;
import com.salon.backstage.pub.bsc.dao.po.Comment;
import com.salon.backstage.pub.bsc.dao.po.Sys;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.dao.support.CommentSupport;
import com.salon.backstage.qcproject.dao.support.CourseSupport;
import com.salon.backstage.qcproject.service.CourseServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2017年1月3日
 * 类说明：
 */
@Service
public class CourseServiceImplNEW implements CourseServiceNEW {

	@Autowired
	private ObjectDao od;
	
	@Autowired
	private IStatisticsService is;

	@Override
	public Code getCoursesTops(int page,int size,int type) {
		Sql sql = CourseSupport.getCourses(type);
		int start = (page - 1)*size;
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), start, size);
		Code result = Code.init(true, 0, "", (list==null?new ArrayList<Map<String, Object>>():list));
		return result;
	}

	@Override
	public Code getCourseVideoDetail(Integer size, String courseId,String userId,Object videoId) {
		int defaultTime = getFreeDefaultTime();
		Sql sqlVideo = CourseSupport.getCourseVideo(courseId, userId, defaultTime, videoId);
		Map<String, Object> courseVideo = od.getObjectBySql(sqlVideo.getSql(), sqlVideo.getParams());
		if(courseVideo==null)
			return Code.init(false, 1, "该课程已经不存在！");
		Sql sqlVideos = CourseSupport.getCourseVideos(courseId);
		List<Map<String, Object>> courseVideos = od.getListBySql(sqlVideos.getSql(), sqlVideos.getParams(), 0, size);
		Map<String, Object> map = CourseSupport.cleanCourseVideos(courseVideo, courseVideos);
		
		// 2017-1-19 添加该视频的评论数量
		Map<String, Object> params = new HashMap<String, Object>();
		String showVideoId = courseVideo.get("id").toString();
		params.put("commed_id",showVideoId);
		params.put("status",Constant.NO_INT);
		int count = od.getPosCount(Comment.class, params);
		map.put("commentCount", count);
		
		is.addStatistics(Statics.STATISTICS_CLICK_COUNT, Statics.STATICS_TYPE_KC,courseId);
		is.addStatistics(Statics.STATISTICS_PLAY_COUNT, Statics.STATICS_TYPE_SP,showVideoId);
		is.addStatistics(Statics.STATISTICS_CLICK_COUNT, Statics.STATICS_TYPE_SP,showVideoId);
		
		return Code.init(true, 0, "", map);
	}

	@Override
	public Code getCourseVideoByVideoId(String courseId, String userId, Object videoId) {
		int defaultTime = getFreeDefaultTime();
		Sql sqlVideo = CourseSupport.getCourseVideo(courseId, userId, defaultTime, videoId);
		Map<String, Object> courseVideo = od.getObjectBySql(sqlVideo.getSql(), sqlVideo.getParams());
		if(courseVideo==null)
			return Code.init(false, 1, "该视频已经不存在！");
		Sql commentSql = CommentSupport.getComments(courseVideo.get("id").toString(), userId);
		List<Map<String, Object>> comments = od.getListBySql(commentSql.getSql(), commentSql.getParams(), 0, 2);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("videoMessage", courseVideo);
		map.put("commentMessage", (comments==null?new ArrayList<Map<String, Object>>():comments));
		
		// 2017-1-22 添加该视频的评论数量
		Map<String, Object> params = new HashMap<String, Object>();
		String showVideoId = courseVideo.get("id").toString();
		params.put("commed_id", showVideoId);
		params.put("status",Constant.NO_INT);
		int count = od.getPosCount(Comment.class, params);
		map.put("commentCount", count);
		
		is.addStatistics(Statics.STATISTICS_PLAY_COUNT, Statics.STATICS_TYPE_SP,showVideoId);
		is.addStatistics(Statics.STATISTICS_CLICK_COUNT, Statics.STATICS_TYPE_SP,showVideoId);
		
		return Code.init(true, 0, "", map);
	}
	
	/**
	 * 获得视频的默认免费观看时长
	 * @return
	 */
	private int getFreeDefaultTime(){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("type", Constant.SYSTEM_TYPE_NUMBER);
		params.put("status", Constant.YES_INT);
		params.put("sys_key", Constant.SYSTEM_KEY_VIDEO_FREESHOW_DEFAULTTIME);
		Sys sys = od.getObjByParams(Sys.class, params);
		int defaultTime = Integer.parseInt(sys.getSys_value());
		return defaultTime;
	}
	
}
