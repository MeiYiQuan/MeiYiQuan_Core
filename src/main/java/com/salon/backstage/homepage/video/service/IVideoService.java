package com.salon.backstage.homepage.video.service;

import java.util.List;
import java.util.Map;

/**
 * 视频表接口
 */
public interface IVideoService {
	
	/**
	 * 首页-视频详情-视频目录接口 
	 */
	List<Map> queryByCourseId(Map<String, Object> json);

	/**
	 * 首页-视频详情页面内部视频之间的跳转
	 */
	Map courseDetailJumpVideoMessage(Map<String, Object> json);

	
	
	
	
}
