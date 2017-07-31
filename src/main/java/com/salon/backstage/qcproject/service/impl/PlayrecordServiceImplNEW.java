package com.salon.backstage.qcproject.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.pub.bsc.dao.po.Playrecord;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.dao.support.CourseSupport;
import com.salon.backstage.qcproject.dao.support.PlayrecordSupport;
import com.salon.backstage.qcproject.service.PlayrecordServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Sql;

/**
 * 作者：齐潮
 * 创建日期：2017年1月7日
 * 类说明：
 */
@Service
public class PlayrecordServiceImplNEW implements PlayrecordServiceNEW {
	
	@Autowired
	private ObjectDao od;
	
	@Override
	public Code saveOrUpdateRecord(String userId, String videoId,String continueTime) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("video_id", videoId);
		params.put("user_id", userId);
		Playrecord record = od.getObjByParams(Playrecord.class, params);
		if(record==null){
			// 将课程查出
			Sql courseSql = CourseSupport.getCourseByVideoId(videoId);
			Map<String, Object> course = od.getObjectBySql(courseSql.getSql(), courseSql.getParams());
			if(course==null)
				return Code.init(false, 3, "该课程已经不存在！");
			record = new Playrecord();
			record.setContinue_time(continueTime);
			record.setCourse_id(course.get("id").toString());
			record.setPlay_time(System.currentTimeMillis());
			record.setUser_id(userId);
			record.setVideo_id(videoId);
			record.setVideo_title(course.get("video_title").toString());
			record.setTime_long(Integer.parseInt(course.get("time_long").toString()));
			String recordId = od.save(record);
			if(recordId==null)
				return Code.init(false, 2, "系统错误！请稍后再试！");
			return Code.init(true, 0, "保存播放记录成功！");
		}
		Map<String,Object> settings = new HashMap<String,Object>();
		settings.put("play_time", System.currentTimeMillis());
		settings.put("continue_time", continueTime);
		int upresult = od.updateById(Playrecord.class, record.getId().toString(), settings);
		if(upresult!=1)
			return Code.init(false, 2, "系统错误！请稍后再试！");
		return Code.init(true, 0, "保存播放记录成功！");
	}

	@Override
	public Code getUserPlayRecords(String userId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("user_id", userId);
		List<Playrecord> list = od.getPos(Playrecord.class, params, null, null, Order.desc("play_time"));
		Map<String,Object> map = new HashMap<String,Object>();
		if(list==null){
			List<Playrecord> nullList = new ArrayList<Playrecord>();
			map.put("weeks", nullList);
			map.put("months", nullList);
			map.put("monthBefores", nullList);
			return Code.init(true, 0, "", map);
		}
		
		List<Playrecord> week = new ArrayList<Playrecord>();
		List<Playrecord> month = new ArrayList<Playrecord>();
		List<Playrecord> beforMonth = new ArrayList<Playrecord>();
		for(int i=0;i<list.size();i++){
			Playrecord record = list.get(i);
			long now = System.currentTimeMillis();
			long playTime = record.getPlay_time();
			
			Calendar weekCalend = Calendar.getInstance();
			weekCalend.setTimeInMillis(now);
			weekCalend.add(Calendar.DATE, -7);
			long weekTime = weekCalend.getTimeInMillis();
			
			if(playTime>=weekTime){
				// 最近一周
				week.add(record);
				continue;
			}
			
			Calendar monthCalend = Calendar.getInstance();
			monthCalend.setTimeInMillis(now);
			monthCalend.add(Calendar.MONTH, -1);
			long monthTime = monthCalend.getTimeInMillis();
			
			if(playTime>=monthTime){
				// 最近一月
				month.add(record);
				continue;
			}
			
			// 剩下的就是一个月之前的
			beforMonth.add(record);
			
		}
		
		map.put("weeks", week);
		map.put("months", month);
		map.put("monthBefores", beforMonth);
		return Code.init(true, 0, "", map);
	}

	@Override
	public Code deleteRecordsByIds(String[] recordIds) {
		Sql sql = PlayrecordSupport.deleteRecordsByIds(recordIds);
		int deleteResult = od.updateBySql(sql);
		if(deleteResult!=recordIds.length)
			return Code.init(false, 1, "删除异常！请刷新查看！");
		return Code.init(true, 0, "删除成功！");
	}

}
