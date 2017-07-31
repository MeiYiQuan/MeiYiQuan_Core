package com.salon.backstage.homepage.follow.service;

import java.util.Map;

import com.salon.backstage.qcproject.util.Code;

/**
 * 收藏表接口
 *
 */
public interface IFollowService {

	/**
	 * 根据用户和讲师的ID查询使用已经关注(0未关注,1已关注)
	 */
	Object queryFollowOrNotByTeacherUserID(Map<String, Object> json);
	
	/**
	 * 处理从讲师详情页面传来的关注请求
	 */
	void changeStatusFromTeacherDetail(Map<String, Object> json);
	/**
	 * 关注
	 * @param json
	 */
	void followed(Map<String, Object> json);
	
	/**
	 * 关注与取消关注
	 * @param userId
	 * @param followId
	 * @param type
	 * @return
	 */
	public Code doing(String userId,String followId,int type);
	
	
}
