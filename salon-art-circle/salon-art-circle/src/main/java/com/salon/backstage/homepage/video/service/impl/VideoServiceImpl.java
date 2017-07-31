package com.salon.backstage.homepage.video.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.homepage.video.service.IVideoService;

@Service
public class VideoServiceImpl implements IVideoService{
	
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@Override
	public List<Map> queryByCourseId(Map<String, Object> json) {
		String courseId = (String)json.get("courseId");
		//0 收费,1启用
		String sql = "select id,title,time_long from tb_video where free = 0 and status = 1 and course_id = '"+courseId+"' order by order_num";
		List<Map> videoList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		return videoList;
	}
	
	@SuppressWarnings("all")
	@Override
	public Map courseDetailJumpVideoMessage(Map<String, Object> json) {
		String videoId = (String)json.get("videoId");
		Map totalMap = new HashMap();
		String videoSql = "select id,title,remark,share_url,video_save_url,video_pic_url,time_long,play_time,per_cost from tb_video where id = '"+videoId+"'";
		Map oneMap = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(videoSql);
		
		//根据oneMap中的视频ID去播放记录表中查询改视频的播放次数
		String playrecordSql = "select count(1) coun from tb_playrecord where video_id = '" + (String)oneMap.get("id")+"'";
		List<Map> playrecordList = extraSpringHibernateTemplate.createSQLQueryFindAll(playrecordSql);
		Object playrecordCount = playrecordList.get(0).get("coun");
		
		totalMap.put("videoBaseMessage", oneMap);
		totalMap.put("playrecordCount", playrecordCount);
		return totalMap;
	}
	
	
}







