package com.salon.backstage.qcproject.service;

import java.util.Map;

import com.salon.backstage.qcproject.util.Code;

/**
 * 作者：齐潮
 * 创建日期：2017年1月3日
 * 类说明：处理有关用户的业务逻辑
 */
public interface UserServiceNEW {

	/**
	 * 修改用户的信息
	 * @param userId
	 * @param settings
	 * @param now
	 * @param birthDay
	 * @return
	 * @throws Exception
	 */
	public Code updateUser(String userId,Map<String,Object> settings,long now,long birthDay) throws Exception;
	
	/**
	 * 获取关于我们信息
	 * @return
	 */
	public Code getAboutUs();
	
	/**
	 * 分页获取讲师的体现记录
	 * @param teacherId
	 * @param page
	 * @param size
	 * @return
	 */
	public Code getTeacherSends(String teacherId,int page,int size);
	
}
