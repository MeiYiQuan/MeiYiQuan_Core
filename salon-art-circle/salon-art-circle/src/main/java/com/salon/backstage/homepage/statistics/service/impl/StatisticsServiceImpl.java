package com.salon.backstage.homepage.statistics.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.homepage.statistics.service.IStatisticsService;
import com.salon.backstage.pub.bsc.dao.po.Statistics;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.util.Sql;

@Service
public class StatisticsServiceImpl implements IStatisticsService {

	@Autowired
	private ObjectDao od;
	
	@Override
	public boolean addStatistics(String dataName,int type,String... ids){
		StringBuffer addIn = new StringBuffer();
		Map<String,Object> params = new HashMap<String,Object>();
		for(int i=0;i<ids.length;i++){
			addIn.append(":id" + i + ",");
			params.put("id" + i, ids[i]);
		}
		String sql = "update `tb_statistics` set `" + dataName + "` = `" + dataName + "` + 1 where `type_id` in (" + addIn.substring(0,addIn.length()-1) + ") and `type` = " + type;
		
		
		Sql s = Sql.get(sql, params);
		int upResult = od.updateBySql(s);
		if(upResult==0){
			// 没有这些数据
			
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(int i=0;i<ids.length;i++){
				Map<String,Object> statics = new HashMap<String,Object>();
				statics.put("type", type);
				statics.put("type_id", ids[i]);
				statics.put(dataName, 1);
				list.add(statics);
			}
			od.saveObjects(Statistics.class, list);
		}
		return true;
	}

	@Override
	public boolean subStatistics(String dataName,int type,String... ids) {
		StringBuffer addIn = new StringBuffer();
		Map<String,Object> params = new HashMap<String,Object>();
		for(int i=0;i<ids.length;i++){
			addIn.append(":id" + i + ",");
			params.put("id" + i, ids[i]);
		}
		String sql = "update `tb_statistics` set `" + dataName + "` = `" + dataName + "` - 1 where `type_id` in (" + addIn.substring(0,addIn.length()-1) + ") and `type` = " + type;
		Sql s = Sql.get(sql, params);
		int upResult = od.updateBySql(s);
		if(upResult==0)
			return false;
		return true;
	}
	
	@Override
	public Statistics getStatistics(String id,int type){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("type_id", id);
		params.put("type", type);
		Statistics statistics = od.getObjByParams(Statistics.class, params);
		if(statistics==null){
			statistics = new Statistics();
			statistics.setType(type);
			statistics.setType_id(id);
			String staId = od.save(statistics);
			if(staId==null)
				return null;
			statistics.setId(staId);
		}
		return statistics;
	}
	
	
	
	
	/*
	@Override
	public Statistics saveStatistics(String id){
		Statistics statistics=new Statistics();
		statistics.setClick_count(0);
		statistics.setClick_expect_count(0);
		statistics.setCollect_count(0);
		statistics.setComment_count(0);
		statistics.setComment_expect_count(0);
		statistics.setCoupon_count(0);
		statistics.setDislike_count(0);
		statistics.setDislike_expect_count(0);
		statistics.setFollow_count(0);
		statistics.setFollow_expect_count(0);
		statistics.setLike_count(0);
		statistics.setLike_expect_count(0);
		statistics.setPlay_expect_count(0);
		statistics.setPlay_count(0);
		statistics.setShare_count(0);
		statistics.setShare_expect_count(0);
		statistics.setSigndays(0);
		statistics.setType(0);
		statistics.setType_id(id);
		extraSpringHibernateTemplate.getHibernateTemplate().save(statistics);
		return statistics;
	}

	@Override
	public Statistics saveStatisticsByType(String id, String type) {
		Statistics statistics=new Statistics();
		statistics.setClick_count(0);
		statistics.setClick_expect_count(0);
		statistics.setCollect_count(0);
		statistics.setComment_count(0);
		statistics.setComment_expect_count(0);
		statistics.setCoupon_count(0);
		statistics.setDislike_count(0);
		statistics.setDislike_expect_count(0);
		statistics.setFollow_count(0);
		statistics.setFollow_expect_count(0);
		statistics.setLike_count(0);
		statistics.setLike_expect_count(0);
		statistics.setPlay_expect_count(0);
		statistics.setPlay_count(0);
		statistics.setShare_count(0);
		statistics.setShare_expect_count(0);
		statistics.setSigndays(0);
		statistics.setType(Integer.valueOf(type));
		statistics.setType_id(id);
		extraSpringHibernateTemplate.getHibernateTemplate().save(statistics);
		return statistics;
	}
	*/
}
