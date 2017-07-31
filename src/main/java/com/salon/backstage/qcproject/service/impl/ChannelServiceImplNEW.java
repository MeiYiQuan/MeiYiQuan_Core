package com.salon.backstage.qcproject.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.pub.bsc.domain.Constant.ChannelTop;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.dao.support.ChannelSupport;
import com.salon.backstage.qcproject.service.ChannelServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Sql;

/**
 * 作者：齐潮
 * 创建日期：2017年1月8日
 * 类说明：
 */
@Service
public class ChannelServiceImplNEW implements ChannelServiceNEW {

	@Autowired
	private ObjectDao od;

	@SuppressWarnings({ "unchecked"})
	@Override
	public Code getChannels(ChannelTop topType, Object channelName, Object district) {
		Sql sql = ChannelSupport.getChannels(topType, channelName, district);
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), null, null);
		if(list==null||list.size()<1)
			return Code.init(true, 0, "", new ArrayList<Map<String, Object>>());
		// 取出所有的一级频道
		List<Map<String,Object>> oneLevels = new ArrayList<Map<String,Object>>();
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			int type = Integer.parseInt(map.get("type").toString());
			if(type==Constant.CHANNEL_ONE_LEVEL){
				
				// 移除多余字段
				map.remove("pid");
				map.remove("enable");
				map.remove("create_time");
				map.remove("update_time");
				map.remove("update_admin_id");
				map.remove("logo_url");
				map.remove("type");
				
				map.put("categoryList", new ArrayList<Map<String,Object>>());
				oneLevels.add(map);
			}
		}
		// 将所有的二级频道拼装到一级频道中
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			Object typeObj = map.get("type");
			if(typeObj!=null&&Integer.parseInt(typeObj.toString())==Constant.CHANNEL_TWO_LEVEL){
				Object pidObj = map.get("pid");
				for(Map<String,Object> oneLevel:oneLevels){
					if(oneLevel.get("id").equals(pidObj)){
						
						// 移除多余字段
						map.remove("pid");
						map.remove("enable");
						map.remove("create_time");
						map.remove("update_time");
						map.remove("update_admin_id");
						map.remove("type");
						
						List<Map<String, Object>> categoryList = (List<Map<String, Object>>) oneLevel.get("categoryList");
						categoryList.add(map);
					}
				}
			}
		}
		
		// 把最后结果中为空的一级频道去除
		List<Map<String, Object>> channels = new ArrayList<Map<String,Object>>();
		for(int i=0;i<oneLevels.size();i++){
			Map<String,Object> map = oneLevels.get(i);
			List<Map<String, Object>> categoryList = (List<Map<String, Object>>) map.get("categoryList");
			if(categoryList.size()>0)
				channels.add(map);
		}
		
		Code result = Code.init(true, 0, "", channels);
		return result;
	}
	
}
