package com.salon.backstage.homepage.channel.service;

import java.util.List;
import java.util.Map;

import com.salon.backstage.pub.bsc.dao.po.Channel;
import com.salon.backstage.pub.bsc.domain.Constant.ChannelTop;

/**
 * 频道表接口
 *
 */
public interface IChannelService {

	/**
	 * 创业开店
	 */
	List<Map<String,Object>> queryStartshopList();

	/**
	 * 潮流技术
	 */
	List<Map<String,Object>> queryTechnologyList();
	
	/**
	 * 根据传入的顶级菜单类型来获取这个顶级菜单下所有的一级菜单和二级菜单，也可以查询出除顶级之外全部的频道
	 * @param topType
	 * @return
	 */
	List<Map<String,Object>> getChannelsByTopType(ChannelTop topType);
	
	/**
	 * 根据id去获取一个channel
	 * @param channelId
	 * @return
	 */
	Channel getChannelById(String channelId);
	
	/**
	 * 根据id去查看一个channel是否存在
	 * @param channelId
	 * @return
	 */
	Map<String,Object> isHave(String channelId);

}
