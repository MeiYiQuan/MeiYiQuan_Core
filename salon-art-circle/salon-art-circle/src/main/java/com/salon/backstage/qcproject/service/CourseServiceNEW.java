package com.salon.backstage.qcproject.service;

import com.salon.backstage.qcproject.util.Code;

/**
 * 作者：齐潮
 * 创建日期：2017年1月3日
 * 类说明：处理有关课程的业务逻辑
 */
public interface CourseServiceNEW {

	/**
	 * 查询出正在热播三大榜单：热播榜，热销榜，热评榜
	 * @param page
	 * @param size
	 * @param type
	 * @return
	 */
	public Code getCoursesTops(int page,int size,int type);
	
	/**
	 * 获取课程所绑定的视频详情，并分页获取该课程的第一页视频，包括所绑定的视频。
	 * videoId为null或者空字符串时，说明是通过点击课程详情来访问，会选择第一个视频来播放。当videoId不为null或者空字符串时，会直接选择这个视频播放。
	 * 暂时将size传为null，表示直接全部查出
	 * @param size
	 * @param courseId
	 * @param userId
	 * @param videoId
	 * @return
	 */
	public Code getCourseVideoDetail(Integer size,String courseId,String userId,Object videoId);
	
	/**
	 * 视频跳转
	 * @param courseId
	 * @param userId
	 * @param videoId
	 * @return
	 */
	public Code getCourseVideoByVideoId(String courseId,String userId,Object videoId);
	
}
