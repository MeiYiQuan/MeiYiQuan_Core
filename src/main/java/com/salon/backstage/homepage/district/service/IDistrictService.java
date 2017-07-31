package com.salon.backstage.homepage.district.service;

import java.util.List;
import java.util.Map;

public interface IDistrictService {

	/**
	 * 返回用户注册时需要的省份信息 的接口
	 */
	List<Map> queryProv();

	/**
	 * 返回用户注册时需要的详细地址信息 的接口
	 */
	List<Map> queryDetail(String districtId);

}
