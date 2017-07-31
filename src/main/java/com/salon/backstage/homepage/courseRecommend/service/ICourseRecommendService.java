package com.salon.backstage.homepage.courseRecommend.service;

import java.util.List;
import java.util.Map;

public interface ICourseRecommendService {
	
	/**
	 * 课程详情页-根据课程详情页传来的courseId获得相关推荐课程的详情
	 */
	List<Map<String, Object>> getCourseRecommend(String courseId);

}
