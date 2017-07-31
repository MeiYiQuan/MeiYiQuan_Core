package com.salon.backstage.homepage.point.service;

import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant.PointEachType;

public interface IPointService {
	
	/**
	 * 登录送积分接口
	 */
	void login(User user);

	/**
	 * 注册送积分接口
	 */
	void pointRegister(String userId);
	/**
	 * 收藏送积分接口
	 * @param userId
	 */
	void pointCollect(String userId);
	/**
	 * 投票送积分接口
	 * @param userId
	 */
	void pointVote(String userId);
	/**
	 * 评论课程送积分接口
	 * @param userId
	 */
	void pointComment(String userId);
	/**
	 * 完善个人信息送积分接口
	 * @param string
	 */
	void pointEditPersion(String userId);
	/**
	 * 完善个人信息送的积分个数
	 * @param string
	 * @return
	 */
	long pointPersion(String userId,int i);
	/**
	 * 上传头像送的积分数
	 * @param string
	 */
	void pointReplaceHead(String string);
	/**
	 * 手机验证送的积分
	 * @param json
	 */
	void pointPhone(String userId);
	
	/**
	 * 公用的增加积分的方法
	 * @param user
	 * @param type
	 * @return
	 * @throws Exception 
	 */
	public MobileMessage addPoint(User user,PointEachType type, double orderPrice) throws Exception;

}












