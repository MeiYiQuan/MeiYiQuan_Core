package com.salon.backstage.qcproject.dao.support;

import com.qc.util.Condition;
import com.qc.util.Condition.Con;
import com.qc.util.Condition.Cs;
import com.qc.util.enums.E;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.pub.bsc.domain.Constant.ChannelTop;
import com.salon.backstage.qcproject.util.Sql;

/**
 * 作者：齐潮
 * 创建日期：2017年1月8日
 * 类说明：用于产生频道相关的sql
 */
public class ChannelSupport {

	/**
	 * 条件获取所有的频道，类型，频道名称，地区。
	 * 地区放在第二阶段做，所以暂时不用管
	 * @param type
	 * @param channelName
	 * @param district
	 * @return
	 */
	public final static Sql getChannels(ChannelTop topType,Object channelName,Object district){
		Cs cs = Condition.getConditions(Con.getCon("`channel`.`name`", E.LIKE, channelName));
		String topId = topType.getTopId();
		String inValue = topId==null?("in ('" + ChannelTop.CHANNEL_TOP_CHAOLIUJISHU.getTopId() + "','" + ChannelTop.CHANNEL_TOP_CHUANGYEKAIDIAN.getTopId() + "')"):"= '" + topId + "'";
		String sql = "select `channel`.*,"
							+ "if(`onelevel`.`id` " + inValue + "," + Constant.CHANNEL_ONE_LEVEL + ",if(`top`.`id` " + inValue + "," + Constant.CHANNEL_TWO_LEVEL + "," + Constant.CHANNEL_UNKNOWN + ")) as `type` "
						+ "from `tb_channel` as `channel` "
						+ "LEFT JOIN `tb_channel` as `onelevel` on `onelevel`.`id` = `channel`.`pid` "
						+ "LEFT JOIN `tb_channel` as `top` on `top`.`id` = `onelevel`.`pid` "
						+ "where 1 = 1 "
							+ "and ((`top`.`id` " + inValue + " " + cs.getConditions() + ") or `onelevel`.`id` " + inValue + ") "
							+ "and `onelevel`.`enable` = " + Constant.YES_INT + " "
							+ "and (`onelevel`.`pid` = '" + Constant.CHANNEL_TOP_PID + "' or `top`.`enable` = " + Constant.YES_INT + ") "
							+ "and `channel`.`enable` = " + Constant.YES_INT + " "
						+ "GROUP BY `channel`.`id` "
						+ "ORDER BY `channel`.`create_time` ASC";
		Sql result = Sql.get(sql, cs.getParams());
		return result;
	}
	
	
}
