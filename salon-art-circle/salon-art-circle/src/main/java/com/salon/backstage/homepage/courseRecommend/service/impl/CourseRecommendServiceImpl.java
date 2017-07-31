package com.salon.backstage.homepage.courseRecommend.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.homepage.courseRecommend.service.ICourseRecommendService;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.dao.support.CourseSupport;
import com.salon.backstage.qcproject.util.Sql;

@SuppressWarnings("all")
@Service
public class CourseRecommendServiceImpl implements ICourseRecommendService {
	
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@Autowired
	private ObjectDao od;
	
	@Override
	public List<Map<String, Object>> getCourseRecommend(String courseId) {
		/*
		String sql = "select recommend_course_id from tb_course_recommend where course_id = '"+courseId+"'";
		List<Map> courRecomIdList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		
		List<Map> courRecomList = new ArrayList();
		Iterator<Map> courRecomIdListIte = courRecomIdList.iterator();
		while(courRecomIdListIte.hasNext()){
			Map courRecomIdMap = courRecomIdListIte.next();
			
			String sqlRecom = "select id,pic_big_url,title,description from tb_course where playing = 1 and status = 1 and id = '"+(String)courRecomIdMap.get("recommend_course_id")+"'";
			Map courseRecomMap = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sqlRecom);
			
			courRecomList.add(courseRecomMap);
		}
		*/
		Sql sql = CourseSupport.getCourseRecommends(courseId);
		List<Map<String, Object>> courRecomList = od.getListBySql(sql.getSql(), sql.getParams(), null, null);
		return courRecomList;
	}
	
	
	
}



































