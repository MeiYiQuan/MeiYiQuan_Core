package com.salon.backstage.qcproject.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.pub.bsc.dao.po.Banner;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.dao.support.HomePageSupport;
import com.salon.backstage.qcproject.service.HomePageServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2017年1月5日
 * 类说明：
 */
@Service
public class HomePageServiceImplNEW implements HomePageServiceNEW {

	@Autowired
	private ObjectDao od;

	@Override
	public Code getHomePageAll() {
		Map<String,Object> bannerParams = new HashMap<String,Object>();
		bannerParams.put("status", Constant.YES_INT);
		bannerParams.put("showtype", Statics.BANNER_SHOWTYPE_HOME);
		List<Map<String, Object>> banners = od.getPosForMap(Banner.class, bannerParams, null, null, Order.desc("order_num"));
		
		Sql teacherSql = HomePageSupport.getHomePages(Statics.HOMEPAGE_TYPE_CELEBRITY);
		Sql kdcySql = HomePageSupport.getHomePages(Statics.HOMEPAGE_TYPE_SHOP);
		Sql cljsSql = HomePageSupport.getHomePages(Statics.HOMEPAGE_TYPE_TECHNOLOGY);
		Sql videoSql = HomePageSupport.getHomePages(Statics.HOMEPAGE_TYPE_VIDEO);
		
		List<Map<String, Object>> teachers = od.getListBySql(teacherSql.getSql(), teacherSql.getParams(), null, null);
		List<Map<String, Object>> kdcys = od.getListBySql(kdcySql.getSql(), kdcySql.getParams(), null, null);
		List<Map<String, Object>> cljss = od.getListBySql(cljsSql.getSql(), cljsSql.getParams(), null, null);
		List<Map<String, Object>> videos = od.getListBySql(videoSql.getSql(), videoSql.getParams(), null, null);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bannerList", (banners==null?new ArrayList<Map<String, Object>>():banners));
		map.put("teacherList", (teachers==null?new ArrayList<Map<String, Object>>():teachers));
		map.put("videoList", (videos==null?new ArrayList<Map<String, Object>>():videos));
		map.put("kdcys", (kdcys==null?new ArrayList<Map<String, Object>>():kdcys));
		map.put("cljss", (cljss==null?new ArrayList<Map<String, Object>>():cljss));
		
		Code result = Code.init(true, 0, "", map);
		
		return result;
	}
	
}
