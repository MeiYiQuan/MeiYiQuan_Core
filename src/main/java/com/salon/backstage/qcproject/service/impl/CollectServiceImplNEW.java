package com.salon.backstage.qcproject.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.homepage.point.service.IPointService;
import com.salon.backstage.homepage.push.service.PushService;
import com.salon.backstage.homepage.statistics.service.IStatisticsService;
import com.salon.backstage.pub.bsc.dao.po.Activity;
import com.salon.backstage.pub.bsc.dao.po.Collect;
import com.salon.backstage.pub.bsc.dao.po.Teacher;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.dao.po.Video;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant.PointEachType;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.dao.support.ActivitySupport;
import com.salon.backstage.qcproject.dao.support.CollectSupport;
import com.salon.backstage.qcproject.service.CollectServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2016年12月26日
 * 类说明：
 */
@Service
public class CollectServiceImplNEW implements CollectServiceNEW {

	@Autowired
	private ObjectDao od;
	
	@Autowired
	private PushService ps;
	
	@Autowired
	private IStatisticsService is;
	
	@Autowired
	private IPointService point;
	
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Code getCollectsByUserId(String userId, int page, int size, int type) {
		Sql sql = CollectSupport.getCollectsSql(userId, type);
		int start = (page - 1)*size;
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), start, size);
		if(type==Statics.COLLECT_TYPE_HD)
			// 活动需要单独处理
			ActivitySupport.cleanStatus(null, list);
		Code result = Code.init(true, 0, "", (list==null?new ArrayList():list));
		return result;
	}

	@Override
	public Code doing(String userId, int type, String typeId) throws Exception {
		// 判断该目标信息是否存在
		Object obj = null;
		int staticsType = -1;
		switch(type){
			case Statics.COLLECT_TYPE_HD:
				obj = od.getObjById(Activity.class, typeId);
				staticsType = Statics.STATICS_TYPE_HD;
				break;
			case Statics.COLLECT_TYPE_MRDK:
				//obj = od.getObjById(Teacher.class, typeId);//讲师ID是teacher_id
				obj = extraSpringHibernateTemplate.findFirstOneByPropEq(Teacher.class, "teacher_id", typeId);
				staticsType = Statics.STATICS_TYPE_JS;
				break;
			case Statics.COLLECT_TYPE_SP:
				obj = od.getObjById(Video.class, typeId);
				staticsType = Statics.STATICS_TYPE_SP;
				break;
		}
		if(obj==null)
			return Code.init(false, 5, "该信息已经不存在！");
		// 判断此操作用户是否存在
		User user = od.getObjById(User.class, userId);
		if(user==null)
			return Code.init(false, 6, "已过时，请重新登陆！");
		// 获取收藏信息，如果没有，则创建。有就删除
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("user_id", userId);
		params.put("collect_type", type);
		params.put("collect_type_id", typeId);
		Collect collect = od.getObjByParams(Collect.class, params);
		if(collect==null){
			collect = new Collect();
			collect.setCollect_time(System.currentTimeMillis());
			collect.setCollect_type(type);
			collect.setCollect_type_id(typeId);
			collect.setUser_id(userId);
			String collectId = od.save(collect);
			if(collectId==null)
				return Code.init(false, 7, "数据库错误！");
//			ps.pushEveryOne(null, Statics.PUSH_TITLE_DEFAULT, Statics.PUSH_TYPE_FORTHWITH_TABLE, "收藏成功测试推送，自定义推送", null, userId);
			// 添加积分
			/*
			MobileMessage addPointResult = point.addPoint(user, PointEachType.COLLECT_POINT, 0);
			int point = -1;
			if(addPointResult.isResult()){
				point = Integer.parseInt(addPointResult.getResponse().toString());
			}
			String addMessage = point==-1?"":"获得了" + point + "个积分！";
			*/
			String addMessage = "";
			
			// 添加统计
			if(staticsType!=-1)
				is.addStatistics(Statics.STATISTICS_COLLECT_COUNT, staticsType, typeId);
			
			return Code.init(true, 0, "收藏成功！" + addMessage);
		}
		int deleteResult = od.myDeleteById(Collect.class, collect.getId().toString());
		if(deleteResult<1)
			return Code.init(false, 7, "数据库错误！");
		
		// 添加统计
		if(staticsType!=-1)
			is.subStatistics(Statics.STATISTICS_COLLECT_COUNT, staticsType, typeId);
		
		return Code.init(true, 0, "取消收藏成功！");
		/*
		Map<String,Object> settings = new HashMap<String,Object>();
		settings.put("like_time", System.currentTimeMillis());
		String message = null;
		int old = like.getLike_dislike();
		if(old==Statics.LIKE_YES){
			settings.put("like_dislike", Statics.LIKE_NOT);
			message = "倒点赞成功！";
		}else if(old==Statics.LIKE_NOT){
			settings.put("like_dislike", Statics.LIKE_YES);
			message = "点赞成功！";
		}else{
			return Code.init(false, 7, "数据库错误！");
		}
		int upResult = od.updateById(Like.class, like.getId().toString(), settings);
		if(upResult<1)
			return Code.init(false, 7, "数据库错误！");
		*/
	}

	@Override
	public Code delete(String userId, int type, String[] collectIds) {
		Sql sql = CollectSupport.deleteByIds(collectIds);
		int result = od.updateBySql(sql);
		if(result!=collectIds.length)
			return Code.init(false, 7, "数据库错误！");
		
		int staticsType = 0;
		switch(type){
			case Statics.COLLECT_TYPE_HD:
				staticsType = Statics.STATICS_TYPE_HD;
				break;
			case Statics.COLLECT_TYPE_MRDK:
				staticsType = Statics.STATICS_TYPE_JS;
				break;
			case Statics.COLLECT_TYPE_SP:
				staticsType = Statics.STATICS_TYPE_SP;
				break;
		}
		
		is.subStatistics(Statics.STATISTICS_COLLECT_COUNT, staticsType, collectIds);
		
		return Code.init(true, 0, "删除成功！");
	}
	
}
