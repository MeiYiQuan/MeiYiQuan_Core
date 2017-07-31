package com.salon.backstage.qcproject.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.pub.bsc.dao.po.ActivityStatus;
import com.salon.backstage.pub.bsc.dao.po.ActivityUser;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.dao.support.ActivitySupport;
import com.salon.backstage.qcproject.dao.support.OrderSupport;
import com.salon.backstage.qcproject.service.ActivityServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2016年12月28日
 * 类说明：
 */
@Service
public class ActivityServiceImplNEW implements ActivityServiceNEW {

	@Autowired
	private ObjectDao od;

	@SuppressWarnings("rawtypes")
	@Override
	public Code getActivitiesByType(String userId,int type, int page, int size) {
		String userAddress = null;
		Map<String, Object> params=new HashMap<>();
		if(type==Statics.ACTIVITY_REQUEST_DISTRICT){
			User user = od.getObjById(User.class, userId);
			if(user==null)
				return Code.init(false, 1, "登陆过期，请重新登陆！");
			String userDistrict = user.getDistrict();
			if(userDistrict!=null&&!userDistrict.trim().equals(""))
				userAddress = userDistrict;
		}
		Sql sql = ActivitySupport.getActivitiesByType(userAddress, type);
		int start = (page-1)*size;
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), start, size);
		if(list==null){
			return Code.init(false, 1, "没有数据");
		}
		for(int i=0;i<list.size();i++){
			Map<String, Object> map=list.get(i);
			String aid=map.get("id").toString();
			String sel=ActivitySupport.orderCount(aid);
			List<Map<String, Object>> list1 = od.getListBySql(sel, params, 0,1 );
			Map<String, Object> map1=list1.get(0);
			String co=map1.get("co").toString();
			map.put("partNum", co);
		}
		
		
		ActivitySupport.cleanStatus(null, list);
		
		Code result = Code.init(true, 0, "", (list==null?new ArrayList():list));
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Code getActivitiesByUserId(String userId, int page,int size) {
		Sql sql = ActivitySupport.getActivitiesByUserId(userId);
		int start = (page-1)*size;
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), start, size);
		ActivitySupport.cleanStatus(null, list);
		Code result = Code.init(true, 0, "", (list==null?new ArrayList():list));
		return result;
	}

	@Override
	public Code comeInActivity(String userId, String activityId) {
		// 验证用户是否存在
		User user = od.getObjById(User.class, userId);
		if(user==null)
			return Code.init(false, 1, "该用户已经不存在！");
		if(user.getUser_state()!=Constant.YES_INT)
			return Code.init(false, 2, "该用户不可用！");
		// 验证该活动的状态
		Map<String,Object> activityParams = new HashMap<String,Object>();
		activityParams.put("activity_id", activityId);
		ActivityStatus actiStatus = od.getObjByParams(ActivityStatus.class, activityParams);
		if(actiStatus==null)
			return Code.init(false, 3, "该活动不存在！");
		if(actiStatus.getIsCancel()==Constant.YES_INT)
			return Code.init(false, 4, "该活动已经取消！");
		long now = System.currentTimeMillis();
		if(now<actiStatus.getEnroll_begin_time())
			return Code.init(false, 5, "报名尚未开始！");
		if(now>actiStatus.getEnroll_end_time())
			return Code.init(false, 6, "报名已经结束！");
		// 查看该用户是否已经报过名
		Map<String,Object> actiUserParams = new HashMap<String,Object>();
		actiUserParams.put("user_id", userId);
		actiUserParams.put("activity_id", activityId);
		ActivityUser au = od.getObjByParams(ActivityUser.class, actiUserParams);
		if(au!=null)
			return Code.init(false, 7, "您在这个活动中已经报过名！");
		au = new ActivityUser();
		au.setActivity_id(activityId);
		au.setEnter_time(now);
		au.setMan_type(user.getUser_type());
//		au.setStatus(status);
		au.setUser_id(userId);
		String auId = od.save(au);
		if(auId==null)
			return Code.init(false, 8, "数据库错误！请稍后再试！");
		return Code.init(true, 0, "报名成功！");
	}

	@Override
	public Code getActivityDetail(String activityId, String userId) {
		cleanActivities(activityId);
		Sql detailSql = ActivitySupport.getActivityDetail(activityId, userId);
		Map<String, Object> detail = od.getObjectBySql(detailSql.getSql(), detailSql.getParams());
		if(detail==null)
			return Code.init(false, 5, "该活动已经不存在！");
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		detailList.add(detail);
		Sql usersSql = ActivitySupport.getActivityUsers(activityId);
		List<Map<String, Object>> userList = od.getListBySql(usersSql.getSql(), usersSql.getParams(), null, null);
		List<Map<String, Object>> comeInUsers = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> jiabins = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> count = new ArrayList<Map<String, Object>>();
		ActivitySupport.cleanActivityUsers(userList, jiabins, comeInUsers);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("activityDetail", detailList);
		map.put("activityParticipatorList", comeInUsers);
		Map<String,Object> countMap = new HashMap<String,Object>();
		countMap.put("count", comeInUsers.size());
		count.add(countMap);
		map.put("activityParticipatorCount", count);
		map.put("guestList", jiabins);
		
		
		
		

		// 验证该活动的状态1:已购买  2:准备中 3:可购买 4:已结束6已取消
		Map<String,Object> activityParams = new HashMap<String,Object>();
		activityParams.put("activity_id", activityId);
		ActivityStatus actiStatus = od.getObjByParams(ActivityStatus.class, activityParams);
		
		if(actiStatus.getIsCancel()==Constant.YES_INT)
			map.put("status", 6);
		long now = System.currentTimeMillis();
		if(now<actiStatus.getEnroll_begin_time())
			map.put("status", 2);
		if(now>actiStatus.getEnroll_end_time())
			map.put("status", 4);
		if(now<actiStatus.getEnroll_end_time()&&now>actiStatus.getEnroll_begin_time())
			map.put("status", 3);
		// 查看该用户是否已经报过名
		Map<String,Object> actiUserParams = new HashMap<String,Object>();
		actiUserParams.put("user_id", userId);
		actiUserParams.put("activity_id", activityId);
		ActivityUser au = od.getObjByParams(ActivityUser.class, actiUserParams);
		if(au!=null)
			map.put("status", 1);
		
		
		
		
		
		return Code.init(true, 0, "", map);
	}

	@Override
	public void cleanActivities(String activityId) {
		Sql sql = OrderSupport.cleanSupport(activityId);
		int count = od.updateBySql(sql);
		if(count==0)
			return;
		Sql sqlSub = ActivitySupport.subPerson(activityId, count);
		od.updateBySql(sqlSub);
	}
	
}
