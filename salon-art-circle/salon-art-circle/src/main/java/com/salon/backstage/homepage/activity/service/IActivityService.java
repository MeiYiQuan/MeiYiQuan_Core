package com.salon.backstage.homepage.activity.service;

import java.util.List;
import java.util.Map;

public interface IActivityService {
	/**
	 * 根据teacherId在活动表中查询活动
	 */
	List<Map<String, Object>> queryByTeacherId(String teacherId);

}
