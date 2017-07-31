package com.salon.backstage.qcproject.dao.support;

import java.util.HashMap;
import java.util.Map;

import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.util.Sql;
import com.salon.backstage.qcproject.util.Statics;

/**
 * 作者：齐潮
 * 创建日期：2017年1月20日
 * 类说明：用于产生与订单有关的sql
 */
public class OrderSupport {

	/**
	 * 删除已经过期的关于某个活动的订单
	 * @param activityId
	 * @return
	 */
	public final static Sql cleanSupport(String activityId){
		long lastTime = System.currentTimeMillis() - Statics.ORDER_ACTIVITY_EXTIME - Statics.ORDER_ACTIVITY_MOREEXTIME;
		String sql = "delete from `tb_order` "
						+ "where `status` = " + Constant.PAY_STATUS_NOPAY + " "
						+ "and `create_time` < " + lastTime + " "
						+ "and `type` = " + Statics.ORDER_TYPE_ACTIVITY + " "
						+ "and `video_id` = :activityId";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("activityId", activityId);
		Sql result = Sql.get(sql, params);
		return result;
	}
}
