package com.salon.backstage.homepage.activity.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.homepage.activity.service.IActivityService;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.dao.support.ActivitySupport;
import com.salon.backstage.qcproject.util.Sql;

@Service
public class ActivityServiceImpl implements IActivityService {
	
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@Autowired
	private ObjectDao od;
	
	@SuppressWarnings("all")
	@Override
	public List<Map<String, Object>> queryByTeacherId(String teacherId) {
		Sql sql = ActivitySupport.getActivityByTeacherId(teacherId);
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), null, null);
		return (list==null?new ArrayList<Map<String, Object>>():list);
		/*
		String sql = "select id,title,activity_time,description,district,organiser,show_pic_url from tb_activity where teacher_id = '"+teacherId+"'";
		List<Map> teacherDetailActivityList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		Iterator<Map> iterator = teacherDetailActivityList.iterator();
		while(iterator.hasNext()){
			Map teacherDetailActivityMap = iterator.next();
			String activityId = (String)teacherDetailActivityMap.get("id");
			String sqlCount = "select count(1) coun from tb_activity_user_relationship where status = 1 and activity_id = '"+activityId+"'";
			List<Map> teacherDetailActivityUserList = extraSpringHibernateTemplate.createSQLQueryFindAll(sqlCount);
			Object activityUserCount = teacherDetailActivityUserList.get(0).get("coun");
			teacherDetailActivityMap.put("activityUserCount", activityUserCount);
		}
		return teacherDetailActivityList;
		*/
    
		
		
	}
}









