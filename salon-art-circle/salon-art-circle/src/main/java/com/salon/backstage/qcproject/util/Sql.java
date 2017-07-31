package com.salon.backstage.qcproject.util;

import java.util.Map;

/**
 * 作者：齐潮
 * 创建日期：2016年12月8日
 * 类说明：用于存储最终的sql以及与其对应的params
 */
public class Sql {

	private String sql;
	
	private Map<String,Object> params;
	
	private Sql(){}
	
	public static Sql get(String sql,Map<String,Object> params){
		Sql se = new Sql();
		se.sql = sql;
		se.params = params;
		return se;
	}

	public String getSql() {
		return sql;
	}

	public Map<String, Object> getParams() {
		return params;
	}
	
}
