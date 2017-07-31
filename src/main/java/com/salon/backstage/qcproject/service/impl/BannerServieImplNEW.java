package com.salon.backstage.qcproject.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.pub.bsc.dao.po.Banner;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.service.BannerServiceNEW;
import com.salon.backstage.qcproject.util.Code;

/**
 * 作者：齐潮
 * 创建日期：2017年1月5日
 * 类说明：
 */
@Service
public class BannerServieImplNEW implements BannerServiceNEW {

	@Autowired
	private ObjectDao od;
	
	@Override
	public Code getBannersByShowType(Map<String, Object> params) {
		List<Map<String, Object>> list = od.getPosForMap(Banner.class, params, null, null, Order.desc("order_num"));
		Code result = Code.init(true, 0, "", (list==null?new ArrayList<Map<String, Object>>():list));
		return result;
	}

}
