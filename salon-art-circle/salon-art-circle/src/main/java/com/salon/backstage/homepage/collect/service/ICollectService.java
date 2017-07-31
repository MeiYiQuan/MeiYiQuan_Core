package com.salon.backstage.homepage.collect.service;

import java.util.Map;

/**
 * 收藏表接口
 */
public interface ICollectService {
	
	/**
	 * 根据用户和讲师的ID查询使用已经收藏(0未收藏,1已收藏)
	 */
	Object queryCollectOrNotByTeacherUserID(Map<String, Object> json);

	/**
	 * 处理从讲师详情页面传来的收藏请求
	 */
	void changeStatusFromTeacherDetail(Map<String, Object> json);
	/**
	 * 收藏
	 * @param json
	 */
	void collect(Map<String, Object> json);
	
}
