package com.salon.backstage.find;

import java.util.List;
import java.util.Map;

public interface IFindService {

	List<Map> queryProvince();

	List<Map> queryCity(Map<String, Object> json);

	List<Map> queryStatus();

}
