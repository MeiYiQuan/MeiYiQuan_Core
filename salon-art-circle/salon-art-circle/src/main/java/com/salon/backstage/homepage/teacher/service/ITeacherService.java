package com.salon.backstage.homepage.teacher.service;

import java.util.List;
import java.util.Map;

import com.salon.backstage.common.util.Paging;

/**
 * 讲师表接口
 *
 */
public interface ITeacherService {
	
	/**
	 * 查询首页名人大佬
	 */
	List<Map> queryAllHomepage();
	
	/**
	 * 查询所有讲师
	 */
	List<Map> qureyAll(Integer page,Integer size);
	
	/**
	 * 根据讲师ID查询讲师信息
	 */
	Map queryDetailAll(String teacherId);
	
	/**
	 * 根据课程ID查询讲师
	 */
	Map queryCourseDetailTeacherMap(String courseId);
	/**
	 * 讲师已录制视频
	 */
	List<Map<String, Object>> queryTeacherRecordVideos(String teacherId,int page,int size);
	/**
	 * 讲师即将上映的视频
	 */
	List<Map<String, Object>> queryUpcomingVideoByTeacherId(String teacherId,int page,int size);
	/**
	 * 讲师请求的视频
	 */
	List<Map<String, Object>> queryAskVideo(String teacherId,int page,int size);
	/**
	 * 讲师已经上映的视频
	 */
	List<Map<String, Object>> queryReleasedVideoByTeacherId(String teacherId,int page,int size);
	/**
	 * 讲师正在投票的视频
	 */
	List<Map<String, Object>> queryTeacherVoteVideos(String teacherId,int page,int size);
	/**
	 * 讲师正在预定线下相关活动
	 */
	List<Map> queryAboutActivity(Map<String, Object> json);
	/**
	 * 参加活动的人数
	 * @param activityId
	 * @return
	 */
	Long queryCountByAcitityId(String activityId);
	
	
	
}
