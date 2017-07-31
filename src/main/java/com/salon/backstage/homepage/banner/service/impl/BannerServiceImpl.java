package com.salon.backstage.homepage.banner.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.homepage.banner.service.IBannerService;

@Service
public class BannerServiceImpl implements IBannerService {

	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@SuppressWarnings({ "rawtypes","unchecked" })
	@Override
	public List<Map> queryAllHomepage() {
		String sql = "select id,name,order_num,pic_redirect_url,jump_type,jump_id,create_time,pic_save_url,remark,update_time from tb_banner where status = ?";
		Object[] values = {1};
		List<Map> bannerList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql, values);
		return bannerList;
	}
}
