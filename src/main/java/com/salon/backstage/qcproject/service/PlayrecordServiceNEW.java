package com.salon.backstage.qcproject.service;

import com.salon.backstage.qcproject.util.Code;

/**
 * 作者：齐潮
 * 创建日期：2017年1月7日
 * 类说明：处理有关播放记录的业务逻辑
 */
public interface PlayrecordServiceNEW {

	/**
	 * 新建或者更新一个播放记录
	 * @param userId
	 * @param videoId
	 * @return
	 */
	public Code saveOrUpdateRecord(String userId,String videoId,String continueTime);
	
	/**
	 * 查询出用户所有的播放记录
	 * @param userId
	 * @return
	 */
	public Code getUserPlayRecords(String userId);
	
	/**
	 * 批量删除用户的播放记录
	 * @param recordIds
	 * @return
	 */
	public Code deleteRecordsByIds(String[] recordIds);
	
}
