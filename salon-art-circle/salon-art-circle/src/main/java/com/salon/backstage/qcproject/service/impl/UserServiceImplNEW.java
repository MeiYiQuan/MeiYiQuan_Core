package com.salon.backstage.qcproject.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qc.util.DateFormate;
import com.salon.backstage.homepage.point.service.IPointService;
import com.salon.backstage.pub.bsc.dao.po.Job;
import com.salon.backstage.pub.bsc.dao.po.Sys;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.pub.bsc.domain.Constant.PointEachType;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.dao.support.UserSupport;
import com.salon.backstage.qcproject.service.UserServiceNEW;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2017年1月3日
 * 类说明：
 */
@Service
public class UserServiceImplNEW implements UserServiceNEW {

	@Autowired
	private ObjectDao od;
	
	@Autowired
	private IPointService ps;

	@Override
	public Code updateUser(String userId, Map<String, Object> settings,long now,long birthDay) throws Exception {
		if(birthDay==0){
			settings.put("ageId", Statics.USER_AGEID_NOBIRTHDAY);
		}else{
			String yearStr = DateFormate.getDateFormateCH(birthDay).substring(0, 4);
			String nowYearStr = DateFormate.getDateFormateCH(now).substring(0, 4);
			int year = Integer.parseInt(yearStr);
			int nowYear = Integer.parseInt(nowYearStr);
			int age = nowYear - year;
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("type", Statics.JOB_TYPE_AGE);
			List<Job> jobs = od.getPos(Job.class, params, null, null);
			if(jobs!=null){
				boolean b = true;
				for(Job job:jobs){
					if(job.getId().equals(Statics.USER_AGEID_NOBIRTHDAY))
						continue;
					String[] strs = job.getJob_name().split(Statics.USER_AGEID_JOB_NAME_INDEX);
					int start = Integer.parseInt(strs[0]);
					int end = Integer.parseInt(strs[1]);
					if(start<=age&&age<=end){
						settings.put("ageId", job.getId());
						b = false;
						break;
					}
				}
				if(b)
					settings.put("ageId", "数据错误");
			}else{
				settings.put("ageId", "数据错误");
			}
		}
		int n = od.updateById(User.class, userId, settings);
		if(n!=1)
			return Code.init(false, 4, "系统繁忙！请稍后再试！");
		
		User user = od.getObjById(User.class, userId);
//		if(!settings.get("pic_save_url").toString().trim().equals("")){
//			ps.addPoint(user, PointEachType.USER_PIC_POINT, 0);
//		}
		
		if(!settings.get("gender").toString().trim().equals("")
				&&!settings.get("district").toString().trim().equals("")
				&&!settings.get("birthday").toString().trim().equals("")){
			ps.addPoint(user, PointEachType.USER_DETAIL_POINT, 0);
		}
		
		return Code.init(true, 0, "修改成功！");
	}

	@Override
	public Code getAboutUs() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("sys_key", Constant.SYSTEM_KEY_ABOUTUS);
		Sys sys = od.getObjByParams(Sys.class, params);
		if(sys==null)
			return Code.init(false, 5, "无数据！", "");
		return Code.init(true, 0, "", sys.getSys_value());
	}

	@Override
	public Code getTeacherSends(String teacherId, int page, int size) {
		Sql sql = UserSupport.getTeacherSends(teacherId);
		int start = ( page - 1 ) * size;
		List<Map<String, Object>> list = od.getListBySql(sql.getSql(), sql.getParams(), start, size);
		return Code.init(true, 0, "", (list==null?new ArrayList<Map<String, Object>>():list));
	}
	
}
