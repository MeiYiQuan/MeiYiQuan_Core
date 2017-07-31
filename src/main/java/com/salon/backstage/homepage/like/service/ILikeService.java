package com.salon.backstage.homepage.like.service;

import java.util.Map;

import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;

/**
 * 点赞表接口
 *
 */
public interface ILikeService {
	
	/**
	 * 根据讲师ID查询其点赞量
	 */
	int queryTeacherLikeCount(String teacherId);

	/**
	 * 处理从即将上映详情页传来的点赞请求
	 */
	MobileMessage clickFromFromPlayingsoon(Map<String, Object> json);

	/**
	 * 点赞或者点倒赞，共用这一个方法
	 * @param userId
	 * @param likeId
	 * @param type
	 * @param likeType
	 * @return
	 */
	public MobileMessage click(String userId,String likeId,int type,int likeType,int index);
	
}
