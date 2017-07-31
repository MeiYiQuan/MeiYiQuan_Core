package com.salon.backstage.homepage.district.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.homepage.district.service.IDistrictService;
import com.salon.backstage.pub.bsc.domain.Constant;

@Service
public class DistrictServiceImpl implements IDistrictService{
	
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@SuppressWarnings("all")
	@Override
	public List<Map> queryProv() {
		String sqlProvince = "select id,name,district_grade from tb_district where pid = '0' and `status` = " + Constant.YES_INT;
		List<Map> provList = extraSpringHibernateTemplate.createSQLQueryFindAll(sqlProvince);
		return provList;
	}
	
	@Override
	public List<Map> queryDetail(String districtId) {
		String sqlDetail = "select id,name,district_grade from tb_district where pid = '"+districtId+"' and `status` = " + Constant.YES_INT;
		List<Map> detailList = extraSpringHibernateTemplate.createSQLQueryFindAll(sqlDetail);
		return detailList;
	}
	
}
