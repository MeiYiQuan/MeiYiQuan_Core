package com.salon.backstage.homepage.collect.service.impl;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.homepage.collect.service.ICollectService;
import com.salon.backstage.homepage.point.service.IPointService;
import com.salon.backstage.pub.bsc.dao.po.Collect;
import com.salon.backstage.pub.bsc.dao.po.Statistics;

@Service
public class CollectServiceImpl implements ICollectService {
	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	@Autowired
	IPointService pointService;

	
	@Override
	public Object queryCollectOrNotByTeacherUserID(Map<String, Object> json) {
		String teacherId = (String)json.get("teacherId");
		String userId = (String)json.get("userid");
		// collect_type 2 说明收藏类型为讲师
		String sql = "SELECT COUNT(1) coun FROM tb_collect WHERE user_id = '"+userId+"' AND collect_type = 2 AND collect_type_id = '"+teacherId+"'";
		List<Map> list = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		return list.get(0).get("coun"); //list.get(0).get("coun")  为0时用户未收藏,为1时用数据说明已经收藏
	}
	
	@Override
	public void changeStatusFromTeacherDetail(Map<String, Object> json) {
		String userId = (String)json.get("userid");
		String teacherId = (String)json.get("teacherId");
		// collect_type 2 说明收藏类型为讲师
		String sql = "SELECT COUNT(1) coun FROM tb_collect WHERE user_id = '"+userId+"' AND collect_type = 2 AND collect_type_id = '"+teacherId+"'";
		// 在收藏表中查询是否有收藏记录,有说明该操作执行的是取消收藏,无说明该操作执行的收藏
		List<Map> list = extraSpringHibernateTemplate.createSQLQueryFindAll(sql);
		Object collectStatusObject = list.get(0).get("coun");
		int collectStatus = Integer.valueOf(collectStatusObject.toString());
		if(collectStatus == 1){
			String sqlDelete = "SELECT * FROM tb_collect WHERE user_id = '"+userId+"' AND collect_type = 2 AND collect_type_id = '"+teacherId+"'";
			List<Collect> collectList = extraSpringHibernateTemplate.createSQLQuery(sqlDelete, Collect.class);
			Collect collect = collectList.get(0);
			extraSpringHibernateTemplate.getHibernateTemplate().delete(collect);
			//统计表收藏-1
			addTotalTeacher( teacherId, 1);
		}else{
			Collect collect = new Collect();
			collect.setUser_id(userId);
			collect.setCollect_type(2);// 收藏类型2(int)为收藏讲师
			collect.setCollect_type_id(teacherId);
			collect.setCollect_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
			extraSpringHibernateTemplate.getHibernateTemplate().save(collect);
			//统计表收藏+1
			addTotalTeacher( teacherId, 2);
			pointService.pointCollect(json.get("userid").toString());//收藏获得的积分
		}
	}
	
	/**
	 * 执行收藏/取消收藏讲师后,统计表数据变化
	 */
	private void addTotalTeacher(String teacherId,int type){
		String sql = "select * from tb_statistics where type = 4 and type_id = '"+teacherId+"'"; //4讲师
		List<Statistics> statisticsList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql,Statistics.class);
		Statistics stat = statisticsList.get(0);
		// 1取消收藏 2收藏
		if(type == 1){
			stat.setCollect_count(stat.getCollect_count()-1);
		}else{
			stat.setCollect_count(stat.getCollect_count()+1);
		}
		extraSpringHibernateTemplate.getHibernateTemplate().update(stat);
	}
	/**
	 * 收藏的接口
	 */
	
	@Override
	public void collect(Map<String, Object> json) {
		String userId = (String)json.get("userid");
		String collectTypeId = (String)json.get("collectTypeId");
		// collect_type 0 说明收藏类型为课程
		String sql = "SELECT * FROM tb_collect WHERE user_id = '"+userId+"' AND collect_type_id = '"+collectTypeId+"'";
		// 在收藏表中查询是否有收藏记录,有说明该操作执行的是取消收藏,无说明该操作执行的收藏
		List<Collect> list = extraSpringHibernateTemplate.createSQLQueryFindAll(sql, Collect.class);
		int collectStatus = list.size();
		if(collectStatus == 1){
			Collect collect = list.get(0);
			extraSpringHibernateTemplate.getHibernateTemplate().delete(collect);
			//统计表收藏-1
			addTotal( collectTypeId,1 );
		}else{
			Collect collect = new Collect();
			collect.setUser_id(userId);
			collect.setCollect_type_id(collectTypeId);
			collect.setCollect_time(Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
			extraSpringHibernateTemplate.getHibernateTemplate().save(collect);
			//统计表收藏+1
			addTotal( collectTypeId,2);
		}
	}
	/**
	 * 执行收藏/取消收藏课程后,统计表数据变化
	 */
	private void addTotal(String collectTypeId, int type) {
		String sql = "select * from tb_statistics where   type_id = '"+collectTypeId+"'"; //1为课程
		List<Statistics> statisticsList = extraSpringHibernateTemplate.createSQLQueryFindAll(sql,Statistics.class);
		Statistics stat = statisticsList.get(0);
		// 1取消收藏 2收藏
		if(type == 1){
			stat.setCollect_count(stat.getCollect_count()-1);
		}else{
			stat.setCollect_count(stat.getCollect_count()+1);
		}
		extraSpringHibernateTemplate.getHibernateTemplate().update(stat);
	}
	
}
