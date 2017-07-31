package com.salon.backstage.homepage.subject.service;

import java.util.List;
import java.util.Map;

/**
 * 专题表接口
 *
 */
public interface ISubjectService {

	/**
	 * 首页-精彩专题
	 */
	List<Map> queryHomepage(Map map);
	

}
