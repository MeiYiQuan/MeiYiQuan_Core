package com.salon.backstage.qcproject.service;

import com.salon.backstage.qcproject.util.Code;

/**
 * 作者：齐潮
 * 创建日期：2016年12月27日
 * 类说明：处理有关求课程的业务逻辑
 */
public interface UserVideoRequestServiceNEW {

	/**
	 * 根据userId分页去获取该用户发起的教程，以发起时间降序排序
	 * @param userId
	 * @param page
	 * @param size
	 * @return
	 */
	public Code getRequestsByUserId(String userId,int page,int size);
	
}
