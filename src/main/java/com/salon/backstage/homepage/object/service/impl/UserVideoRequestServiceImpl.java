package com.salon.backstage.homepage.object.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.homepage.channel.service.ObjectService;
import com.salon.backstage.homepage.object.service.UserVideoRequestService;
import com.salon.backstage.pub.bsc.dao.po.Sys;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.pub.bsc.domain.Constant.ChannelTop;
import com.salon.backstage.qcproject.util.Enums.RequestStatus;

/**
 * 作者：齐潮
 * 创建日期：2016年12月16日
 * 类说明：
 */
@Service
public class UserVideoRequestServiceImpl implements UserVideoRequestService {

	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@Autowired
	private ObjectService os;
	
	
	@Override
	public MobileMessage getRequestList(int page, int size, ChannelTop top) {
		String topId = top.getTopId();
		if(topId==null)
			return MobileMessageCondition.addCondition(false, 98, "参数不合法！", null);
		String inStr = RequestStatus.REQUEST_STATUS_DOING.getType() + ""; // + "," + RequestStatus.REQUEST_STATUS_INREVIEW.getType();
		String sql = "select `uvr`.`id` as `id`,`uvr`.`course_name` as `coursename`,`uvr`.`pic_url` as `url`,"
						+ "		`uvr`.`feedback_status` as `status`,`uvr`.`createdTime` as `createdTime`,"
						+ "		`uvr`.`share_url` as `share_url`,"
						+ "		`teac`.`name` as `teachname`,`uvr`.`question` as `question`,"
						+ "		`uvr`.`request_time` as `requesttime`,`uvr`.`vote` as `vote` "
						+ "from `tb_user_video_request` as `uvr` "
						+ "LEFT JOIN `tb_user` as `user` on `user`.`id` = `uvr`.`user_id` "
						+ "LEFT JOIN `tb_teacher` as `teac` on `teac`.`teacher_id` = `uvr`.`teacher_id` "
						+ "where `uvr`.`top_channel_id` = '" + topId + "' "
								+ "and `uvr`.`feedback_status` in (" + inStr + ") "
						+ "group by `uvr`.`id` "
						+ "order by `uvr`.`vote` desc";
		int start = (page-1)*size;
		List list = extraSpringHibernateTemplate.createSQLQueryFindPaging(sql, start, size);
		if(list!=null&&list.size()>0){
			for(Object obj:list){
				Map<String,Object> map = (Map<String, Object>) obj;
				int status = Integer.parseInt(map.get("status").toString());
				map.put("statusStr",RequestStatus.getRequestStatus(status).getMessage());
			}
		}
		return MobileMessageCondition.addCondition(true, 0, "", list);
	}


	@Override
	public MobileMessage getTop() {
		String inStr = RequestStatus.REQUEST_STATUS_DOING.getType() + "," + RequestStatus.REQUEST_STATUS_INREVIEW.getType();
		
		/*String sql = "select `uvr`.`id` as `id`,`uvr`.`course_name` as `coursename`,`uvr`.`pic_url` as `url`,"
				+ "		`uvr`.`feedback_status` as `status`,`uvr`.`createdTime` as `createdTime`,"
				+ "		`uvr`.`share_url` as `share_url`,"
				+ "		`teac`.`name` as `teachname`,`uvr`.`question` as `question`,"
				+ "		`uvr`.`request_time` as `requesttime`,`uvr`.`vote` as `vote` "
				+ "from `tb_user_video_request` as `uvr` "
				+ "LEFT JOIN `tb_user` as `user` on `user`.`id` = `uvr`.`user_id` "
				+ "LEFT JOIN `tb_teacher` as `teac` on `teac`.`teacher_id` = `uvr`.`teacher_id` "
				+ "where `uvr`.`feedback_status` in (" + inStr + ") "
				+ "order by `uvr`.`vote` desc,`uvr`.`createdTime` asc";*/
		
		String sql = "select `uvr`.`id` as `id`,`uvr`.`course_name` as `coursename`,`uvr`.`pic_url` as `url`,"
				+ "		`uvr`.`feedback_status` as `status`,`uvr`.`createdTime` as `createdTime`,"
				+ "		`uvr`.`share_url` as `share_url`,"
				+ "		`teac`.`name` as `teachname`,`uvr`.`question` as `question`,"
				+ "		`uvr`.`request_time` as `requesttime`,`uvr`.`vote` as `vote` "
				+ "from `tb_user_video_request` as `uvr` "
				+ "LEFT JOIN `tb_user` as `user` on `user`.`id` = `uvr`.`user_id` "
				+ "LEFT JOIN `tb_teacher` as `teac` on `teac`.`teacher_id` = `uvr`.`teacher_id` "
				+ "where  `uvr`.top_type=1 "
				+ "order by `uvr`.`createdTime` desc";
		List list = extraSpringHibernateTemplate.createSQLQueryFindPaging(sql, 0, 1);
		if(list==null||list.size()<1){
			Map<String,Object> map = new HashMap<String,Object>();
			Sys sys = os.get(Sys.class, new String[]{"sys_key","status","type"}, new Object[]{Constant.SYSTEM_KEY_REQUEPICURL,Constant.YES_INT,Constant.SYSTEM_TYPE_PHOTO});
			map.put("url", sys.getSys_value());
			map.put("isHave", Constant.NO_INT);
			return MobileMessageCondition.addCondition(true, 0, null, map);
		}else{
			Map<String,Object> map = (Map<String, Object>) list.get(0);
			int status = Integer.parseInt(map.get("status").toString());
			map.put("statusStr",RequestStatus.getRequestStatus(status).getMessage());
			map.put("isHave", Constant.YES_INT);
			return MobileMessageCondition.addCondition(true, 0, null, map);
		}
	}

	
}
