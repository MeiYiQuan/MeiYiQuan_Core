package com.salon.backstage.qcproject.dao.support;

import java.util.HashMap;
import java.util.Map;

import com.salon.backstage.qcproject.util.Sql;

/**
 * 作者：齐潮
 * 创建日期：2017年1月9日
 * 类说明：用于产生于播放记录有关的sql
 */
public class PlayrecordSupport {

	/**
	 * 通过id去批量删除播放记录
	 * @param ids
	 * @return
	 */
	public final static Sql deleteRecordsByIds(String[] ids){
		StringBuffer sql = new StringBuffer("delete from `tb_playrecord` where `id` in (");
		Map<String,Object> params = new HashMap<String,Object>();
		for(int i=0;i<ids.length;i++){
			sql.append(":id" + i + ",");
			params.put("id" + i, ids[i]);
		}
		Sql result = Sql.get(sql.substring(0, sql.length()-1) + ")", params);
		return result;
	}
	
}
