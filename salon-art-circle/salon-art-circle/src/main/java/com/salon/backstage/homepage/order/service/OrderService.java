package com.salon.backstage.homepage.order.service;

import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant.PayStyle;

/**
 * 作者：齐潮
 * 创建日期：2016年12月23日
 * 类说明：处理与订单有关的业务逻辑
 */
public interface OrderService {

	/**
	 * 生成一个订单
	 * @param userId
	 * @param videoId
	 * @param payStyle
	 * @return
	 */
	public MobileMessage createOrder(String userId,int isUseCoupon,String couponId,String shopId,int type,PayStyle payStyle);
	
	/**
	 * 获取当前最大的订单编号，如果没有订单，则返回null
	 * @return
	 */
	public String getMaxOrderNum();
}
