package com.salon.backstage.qcproject.service;

import java.util.Map;

import com.salon.backstage.qcproject.util.Code;

/**
 * 作者：齐潮
 * 创建日期：2017年1月5日
 * 类说明：处理有关banner的业务逻辑
 */
public interface BannerServiceNEW {

	/**
	 * 条件获取banner集合，自动以order_num从小到大排序
	 * @param params
	 * @return
	 */
	public Code getBannersByShowType(Map<String,Object> params);
	
}
