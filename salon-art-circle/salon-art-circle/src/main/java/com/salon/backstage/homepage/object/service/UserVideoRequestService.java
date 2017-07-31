package com.salon.backstage.homepage.object.service;

import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant.ChannelTop;

/**
 * 作者：齐潮
 * 创建日期：2016年12月16日
 * 类说明：处理有关活动表的业务逻辑
 */
public interface UserVideoRequestService {

	/**
	 * 分页获取相应顶级频道下的活动列表
	 * @param page
	 * @param size
	 * @param top
	 * @return
	 */
	public MobileMessage getRequestList(int page,int size,ChannelTop top);
	
	/**
	 * 获得榜首
	 * @return
	 */
	public MobileMessage getTop();
	
	
}
