package com.salon.backstage.qcproject.service;

import com.salon.backstage.pub.bsc.domain.Constant.ChannelTop;
import com.salon.backstage.qcproject.util.Code;

/**
 * 作者：齐潮
 * 创建日期：2017年1月8日
 * 类说明：处理有关频道的业务逻辑
 */
public interface ChannelServiceNEW {

	/**
	 * 根据类型，频道名称，地区来条件搜索频道列表
	 * @param type
	 * @param channelName
	 * @param district
	 * @return
	 */
	public Code getChannels(ChannelTop type,Object channelName,Object district);
	
}
