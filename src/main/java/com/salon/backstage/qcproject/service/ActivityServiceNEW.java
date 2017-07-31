package com.salon.backstage.qcproject.service;

import com.salon.backstage.qcproject.util.Code;

/**
 * 作者：齐潮
 * 创建日期：2016年12月28日
 * 类说明：处理有关活动的业务逻辑
 */
public interface ActivityServiceNEW {

	/**
	 * 根据类型分页去获取线下活动列表
	 * @param userId
	 * @param type
	 * @param page
	 * @param size
	 * @return
	 */
	public Code getActivitiesByType(String userId,int type,int page,int size);
	
	/**
	 * 我的参与
	 * @param userId
	 * @param type
	 * @param page
	 * @return
	 */
	public Code getActivitiesByUserId(String userId,int page,int size);
	
	/**
	 * 发现-线下活动详情：立即参与
	 * @param userId
	 * @param activityId
	 * @return
	 */
	public Code comeInActivity(String userId,String activityId);
	
	/**
	 * 获得某个活动的详情，并携带该用户是否已经购买过该活动的字段
	 * @param activityId
	 * @param userId
	 * @return
	 */
	public Code getActivityDetail(String activityId,String userId);
	
	/**
	 * 更新指定活动的当前参与人数。
	 * 处理机制：获取所有的关于该活动的已经失效的订单。
	 * 删除这些订单，并且在相应的活动中的参与人数进行减去相应数值的操作。
	 * 活动订单的有效期设置在了静态量里。
	 * ping++在生成charge时也会用到这个关于有效期的静态量
	 */
	public void cleanActivities(String activityId);
	
}
