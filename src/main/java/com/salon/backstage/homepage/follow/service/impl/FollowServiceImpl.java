package com.salon.backstage.homepage.follow.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.homepage.follow.service.IFollowService;
import com.salon.backstage.homepage.statistics.service.IStatisticsService;
import com.salon.backstage.pub.bsc.dao.po.Follow;
import com.salon.backstage.pub.bsc.dao.po.Statistics;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Statics;

@Service
public class FollowServiceImpl implements IFollowService{
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@Autowired
	private IStatisticsService is;
	
	@Autowired
	private ObjectDao od;
	
	@Override
	public Object queryFollowOrNotByTeacherUserID(Map<String, Object> json) {
		String teacherId = (String)json.get("teacherId");
		String userId = (String)json.get("userid");
		String sql = "SELECT COUNT(1) coun FROM tb_follow WHERE user_id = '"+userId+"' AND follow_type = 3 AND follow_type_id = '"+teacherId+"'";
		List<Map> list = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		return list.get(0).get("coun"); //list.get(0).get("coun")  为0时用户未关注,为1时用数据说明已经关注
	}
	
	@Override
	public void changeStatusFromTeacherDetail(Map<String, Object> json) {
		String userId = (String)json.get("userid");
		String teacherId = (String)json.get("teacherId");
		String sql = "SELECT COUNT(1) coun FROM tb_follow WHERE user_id = '"+userId+"' AND follow_type = 3 AND follow_type_id = '"+teacherId+"'";
		List<Map> list = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		Object followStatusObject = list.get(0).get("coun");
		int followStatus = Integer.valueOf(followStatusObject.toString());
		if(followStatus == 1){
			String sqlDelete = "SELECT * FROM tb_follow WHERE user_id = '"+userId+"' AND follow_type = 3 AND follow_type_id = '"+teacherId+"'";
			List<Follow> followList = extraSpringHibernateTemplate.createSQLQuery(sqlDelete, Follow.class);
			Follow follow = followList.get(0);
			extraSpringHibernateTemplate.getHibernateTemplate().delete(follow);
			//统计表 关注量-1
			addTotal( teacherId, 1);
		}else{
			Follow follow = new Follow();
			follow.setUser_id(userId);
			follow.setFollow_type(3);// 关注类型3(int)为关注讲师
			follow.setFollow_type_id(teacherId);
			follow.setFollow_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
			extraSpringHibernateTemplate.getHibernateTemplate().save(follow);
			//统计表 关注量+1
			addTotal( teacherId, 2);
		}
	}
	
	/**
	 * 执行关注/取消关注后,统计表数据变化
	 */
	private void addTotal(String teacherId,int type){
		String sql = "select * from tb_statistics where type = 4 and type_id = '"+teacherId+"'";
		List<Statistics> statisticsList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql,Statistics.class);
		Statistics stat = statisticsList.get(0);
		// 1取消收藏 2收藏
		if(type == 1){
			stat.setFollow_count(stat.getFollow_count()-1);
		}else{
			stat.setFollow_count(stat.getFollow_count()+1);
		}
		extraSpringHibernateTemplate.getHibernateTemplate().update(stat);
	}
	
	/**
	 * 关注
	 */
	@Override
	public void followed(Map<String, Object> json) {
		String userId = (String)json.get("userid");
		String followedId = (String)json.get("followedId");
		String sql = "SELECT COUNT(1) coun FROM tb_follow WHERE user_id = '"+userId+"'  AND follow_type_id = '"+followedId+"'";
		List<Map> list = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		Object followStatusObject = list.get(0).get("coun");
		int followStatus = Integer.valueOf(followStatusObject.toString());
		if(followStatus == 1){
			String sqlDelete = "SELECT * FROM tb_follow WHERE user_id = '"+userId+"'  AND follow_type_id = '"+followedId+"'";
			List<Follow> followList = extraSpringHibernateTemplate.createSQLQuery(sqlDelete, Follow.class);
			Follow follow = followList.get(0);
			extraSpringHibernateTemplate.getHibernateTemplate().delete(follow);
			//统计表 关注量-1
//			addTotal(followedId, 1);
			is.subStatistics(Statics.STATISTICS_FOLLOW_COUNT, Statics.STATICS_TYPE_JS, followedId);
		}else{
			Follow follow = new Follow();
			follow.setUser_id(userId);
			follow.setFollow_type_id(followedId);
			follow.setFollow_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
			extraSpringHibernateTemplate.getHibernateTemplate().save(follow);
			//统计表 关注量+1
//			addTotals(followedId, 2);
			is.addStatistics(Statics.STATISTICS_FOLLOW_COUNT, Statics.STATICS_TYPE_JS, followedId);
		}
	}
	
	/**
	 * 执行关注/取消关注后,统计表数据变化
	 */
	private void addTotals(String followedId,int type){
		String sql = "select * from tb_statistics where  type_id = '"+followedId+"'";
		List<Statistics> statisticsList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql,Statistics.class);
		Statistics stat = statisticsList.get(0);
		// 1取消收藏 2收藏
		if(type == 1){
			stat.setFollow_count(stat.getFollow_count()-1);
		}else{
			stat.setFollow_count(stat.getFollow_count()+1);
		}
		extraSpringHibernateTemplate.getHibernateTemplate().update(stat);
	}

	@Override
	public Code doing(String userId, String followId,int type) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("user_id", userId);
		params.put("follow_type", type);
		params.put("follow_type_id", followId);
		Follow follow = od.getObjByParams(Follow.class, params);
		long now = System.currentTimeMillis();
		if(follow==null){
			// 关注
			follow = new Follow();
			follow.setFollow_time(now);
			follow.setFollow_type(type);
			follow.setFollow_type_id(followId);
			follow.setUser_id(userId);
			String fId = od.save(follow);
			if(fId==null)
				return Code.init(false, 5, "系统繁忙！请稍后再试！");
			// 统计加关注量1
			statics(1, type, followId);
			return Code.init(true, 0, "关注成功！");
		}else{
			// 取消关注
			int deleResult = od.myDeleteById(Follow.class, follow.getId().toString());
			if(deleResult!=1)
				return Code.init(false, 5, "系统繁忙！请稍后再试！");
			// 统计减关注量1
			statics(-1, type, followId);
			return Code.init(true, 0, "取消关注成功！");
		}
	}
	
	/**
	 * 关注时对类型进行判断
	 * @param addOrSub
	 * @param type
	 * @param followId
	 */
	private void statics(int addOrSub,int type,String followId){
		if(addOrSub==1){
			switch(type){
			case 3:
				is.addStatistics(Statics.STATISTICS_FOLLOW_COUNT, Statics.STATICS_TYPE_JS, followId);
				break;
			}
		}else if(addOrSub==-1){
			switch(type){
			case 3:
				is.subStatistics(Statics.STATISTICS_FOLLOW_COUNT, Statics.STATICS_TYPE_JS, followId);
				break;
			}
		}
	}
	
}










