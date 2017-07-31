package com.salon.backstage.homepage.channel.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.homepage.channel.service.IChannelService;
import com.salon.backstage.pub.bsc.dao.po.Channel;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.pub.bsc.domain.Constant.ChannelTop;


@Service
public class ChannelServiceImpl implements IChannelService{
	
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	/**
	 * 创业开店
	 */
	@SuppressWarnings("all")
	@Override
	public List<Map<String,Object>> queryStartshopList() {
		List<Map<String,Object>> result = channelsTree(ChannelTop.CHANNEL_TOP_CHUANGYEKAIDIAN);
		return result;
		/*
		String sqlSecond = "select id,name from tb_channel where pid = '1'";
		List<Map> channelStartshopSecondList = extraSpringHibernateTemplate.createSQLQueryFindAll(sqlSecond);
		Iterator<Map> startshopSecondIter = channelStartshopSecondList.iterator();
		while(startshopSecondIter.hasNext()){
			Map groupMap = startshopSecondIter.next();
			String sqlThird = "select id,name,logo_url from tb_channel where pid = '"+(String)groupMap.get("id")+"'";
			List<Map> channelStartshopThirdList = extraSpringHibernateTemplate.createSQLQueryFindAll(sqlThird);
			groupMap.put("categoryList", channelStartshopThirdList);
		}
		return channelStartshopSecondList;
		*/
	}
	
	/**
	 * 潮流技术
	 */
	@SuppressWarnings("all")
	@Override
	public List<Map<String,Object>> queryTechnologyList() {//潮流技术
		List<Map<String,Object>> result = channelsTree(ChannelTop.CHANNEL_TOP_CHAOLIUJISHU);
		return result;
		/*
		String sqlSecond = "select id,name from tb_channel where pid = '2'";
		List<Map> channelTechnologySecondList = extraSpringHibernateTemplate.createSQLQueryFindAll(sqlSecond);
		
		Iterator<Map> technologySecondIter = channelTechnologySecondList.iterator();
		while(technologySecondIter.hasNext()){
			Map groupMap = technologySecondIter.next();
			
			String sqlThird = "select id,name,logo_url from tb_channel where pid = '"+(String)groupMap.get("id")+"'";
			List<Map> channelTechnologyThirdList = extraSpringHibernateTemplate.createSQLQueryFindAll(sqlThird);
			groupMap.put("categoryList", channelTechnologyThirdList);
		}
		return channelTechnologySecondList;
		*/
	}

	/**
	 * 根据频道顶级类型来获得树形的频道集合，如果一个频道都没有，返回null
	 * @param topType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String,Object>> channelsTree(ChannelTop topType){
		List<Map<String, Object>> chuangyeChildrens = getChannelsByTopType(topType);
		if(chuangyeChildrens==null||chuangyeChildrens.size()<1)
			return null;
		// 取出所有的一级频道
		List<Map<String,Object>> oneLevels = new ArrayList<Map<String,Object>>();
		for(int i=0;i<chuangyeChildrens.size();i++){
			Map<String,Object> map = chuangyeChildrens.get(i);
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
		for(int i=0;i<chuangyeChildrens.size();i++){
			Map<String,Object> map = chuangyeChildrens.get(i);
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
		// 将oneLevels抛出
		return oneLevels;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> getChannelsByTopType(ChannelTop topType) {
		String topId = topType.getTopId();
		String inValue = topId==null?("in ('" + ChannelTop.CHANNEL_TOP_CHAOLIUJISHU.getTopId() + "','" + ChannelTop.CHANNEL_TOP_CHUANGYEKAIDIAN.getTopId() + "')"):"= '" + topId + "'";
		String sql = "select `channel`.*,"
						+ "		if(`onelevel`.`id` " + inValue + "," + Constant.CHANNEL_ONE_LEVEL + ",if(`top`.`id` " + inValue + "," + Constant.CHANNEL_TWO_LEVEL + "," + Constant.CHANNEL_UNKNOWN + ")) as `type` "
						+ "from `tb_channel` as `channel` "
						+ "LEFT JOIN `tb_channel` as `onelevel` on `onelevel`.`id` = `channel`.`pid` "
						+ "LEFT JOIN `tb_channel` as `top` on `top`.`id` = `onelevel`.`pid` "
						+ "where 1 = 1 "
						+ (topId==null?"and `onelevel`.`id` is not null ":"and (`top`.`id` = '" + topId + "' or `onelevel`.`id` = '" + topId + "') ")
						+ "		and `onelevel`.`enable` = " + Constant.YES_INT + " "
						+ " 	and (`onelevel`.`pid` = '" + Constant.CHANNEL_TOP_PID + "' or `top`.`enable` = " + Constant.YES_INT + ") "
						+ "		and `channel`.`enable` = " + Constant.YES_INT + " "
						+ "GROUP BY `channel`.`id` "
						+ "ORDER BY `channel`.`create_time` ASC";
		List<Map<String,Object>> channels = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		return channels;
	}

	@Override
	public Channel getChannelById(String channelId) {
		Channel channel = extraSpringHibernateTemplate.findFirstOneByPropEq(Channel.class, new String[]{"id","enable"}, new Object[]{channelId,Constant.YES_INT});
		return channel;
	}

	@Override
	public Map<String, Object> isHave(String channelId) {
		String sql = "select `channel`.*,`onelevel`.`id` as `parentId`,`onelevel`.`pid` as `topId`,`top`.`id` as `tid` "
						+ "from `tb_channel` as `channel` "
						+ "LEFT JOIN `tb_channel` as `onelevel` on `onelevel`.`id` = `channel`.`pid` "
						+ "LEFT JOIN `tb_channel` as `top` on `top`.`id` = `onelevel`.`pid` "
						+ "where `channel`.`id` = ?";
		Map<String,Object> map = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql, new Object[]{channelId});
		Map<String,Object> result = new HashMap<String,Object>();
		if(map==null||map.size()<1||map.get("id")==null||map.get("id").equals("")){
			result.put("isHave", false);
			return result;
		}
		String topId = null;
		String parentId = map.get("pid").toString();
		if(!Constant.CHANNEL_TOP_PID.equals(parentId)){
			Object parentIdObj = map.get("parentId");
			if(parentIdObj==null){
				result.put("isHave", false);
				return result;
			}
			Object topIdObj = map.get("topId");
			if(!Constant.CHANNEL_TOP_PID.equals(topIdObj)){
				Object tidObj = map.get("tid");
				if(tidObj==null){
					result.put("isHave", false);
					return result;
				}
				topId = tidObj.toString();
			}else{
				topId = parentId;
			}
		}else{
			topId = channelId;
		}
		result.put("isHave", true);
		result.put("topId", topId);
		return result;
	}

}













