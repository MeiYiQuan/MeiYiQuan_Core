package com.salon.backstage.qcproject.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qc.util.DateFormate;
import com.salon.backstage.homepage.push.service.PushService;
import com.salon.backstage.pub.bsc.dao.po.Job;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.service.TimerService;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2017年2月28日
 * 类说明：
 */
@Service
public class TimerServiceImpl implements TimerService {

	@Autowired
	private ObjectDao od;
	
	@Autowired
	private PushService ps;
	
	@Override
	public int updateUserAge() {
		List<User> users = od.getPos(User.class, null, null, null);
		if(users==null)
			return 0;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("type", Statics.JOB_TYPE_AGE);
		List<Job> jobs = od.getPos(Job.class, params, null, null);
		int result = 0;
		if(jobs!=null){
			StringBuffer str = new StringBuffer("update `tb_user` set `ageId` = case `id` ");
			StringBuffer ids = new StringBuffer("(");
			long now = System.currentTimeMillis();
			String nowYearStr = DateFormate.getDateFormateCH(now).substring(0, 4);
			for(User user:users){
				str.append("when '" + user.getId() + "' ");
				ids.append("'" + user.getId() + "',");
				if(user.getBirthday()==0){
					str.append("then '" + Statics.USER_AGEID_NOBIRTHDAY + "' ");
					continue;
				}
				String yearStr = DateFormate.getDateFormateCH(user.getBirthday()).substring(0, 4);
				int year = Integer.parseInt(yearStr);
				int nowYear = Integer.parseInt(nowYearStr);
				int age = nowYear - year;
				boolean b = true;
				for(Job job:jobs){
					if(job.getId().equals(Statics.USER_AGEID_NOBIRTHDAY))
						continue;
					String[] strs = job.getJob_name().split(Statics.USER_AGEID_JOB_NAME_INDEX);
					int start = Integer.parseInt(strs[0]);
					int end = Integer.parseInt(strs[1]);
					if(start<=age&&age<=end){
						str.append("then '" + job.getId() + "' ");
						b = false;
						break;
					}
				}
				if(b)
					str.append("then '数据错误' ");
			}
			String idsStr = ids.substring(0, ids.length()-1) + ")";
			str.append("end where `id` in " + idsStr);
			result = od.updateBySql(Sql.get(str.toString(), null));
		}else{
			String sql = "update `tb_user` set `ageId` = '数据错误'";
			result = od.updateBySql(Sql.get(sql, null));
		}
		return result;
	}

	@Override
	public int updateUserCoupons() {
		long now = System.currentTimeMillis();
		String sql = "update `tb_coupon_user` set `status` = " + Constant.NO_INT + " "
						+ "where `isForever` = " + Constant.NO_INT + " "
						+ "and `expire_time` <= " + now + " "
						+ "and `status` = " + Constant.YES_INT;
		int result = od.updateBySql(Sql.get(sql, null));
		return result;
	}

	@Override
	public int updateUserPoints() {
		long now = System.currentTimeMillis();
		String sql = "update `tb_point` set `status` = " + Constant.NO_INT + " "
						+ "where `expire_time` <= " + now + " "
						+ "and `status` = " + Constant.YES_INT;
		int result = od.updateBySql(Sql.get(sql, null));
		return result;
	}

	@Override
	public int sendCouponWarningToUsers() {
		String now = DateFormate.getDateFormateNoTime(System.currentTimeMillis());
		String sql = "select ifnull(group_concat(DISTINCT if(`user_id` = '',null,`user_id`)),'') as `userIds` "
					+ "from `tb_coupon_user` "
					+ "where `isForever` = " + Constant.NO_INT + " "
						+ "and `status` = " + Constant.YES_INT + " "
						+ "and FROM_UNIXTIME(`warn_time`/1000,'%Y-%m-%d') = '" + now + "'";
		Map<String, Object> map = od.getObjectBySql(sql, null);
		if(map==null)
			return 0;
		String[] userIds = map.get("userIds").toString().split(",");
		int result = userIds.length;
		if(result>0)
			ps.pushEveryOne("", "优惠券到期提醒", Statics.PUSH_TYPE_FORTHWITH_NOTABLE, "您有优惠券即将到期，请尽快使用！", null, userIds);
		return result;
	}

	@Override
	public int sendPointWarningToUsers() {
		String now = DateFormate.getDateFormateNoTime(System.currentTimeMillis());
		String sql = "select ifnull(group_concat(DISTINCT if(`user_id` = '',null,`user_id`)),'') as `userIds` "
						+ "from `tb_point` "
						+ "where `status` = " + Constant.YES_INT + " "
						+ "and FROM_UNIXTIME(`warn_time`/1000,'%Y-%m-%d') = '" + now + "' "
						+ "and `point_unused` > 0 ";
		Map<String, Object> map = od.getObjectBySql(sql, null);
		if(map==null)
			return 0;
		String[] userIds = map.get("userIds").toString().split(",");
		int result = userIds.length;
		if(result>0)
			ps.pushEveryOne("", "积分到期提醒", Statics.PUSH_TYPE_FORTHWITH_NOTABLE, "您有积分即将到期，请尽快使用！", null, userIds);
		return result;
	}

}
