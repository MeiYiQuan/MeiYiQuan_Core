package com.salon.backstage.homepage.banner.service;

import java.util.List;
import java.util.Map;

/**
 * 录播图表接口
 */
public interface IBannerService {

	@SuppressWarnings("rawtypes")
	List<Map> queryAllHomepage();
	
}
