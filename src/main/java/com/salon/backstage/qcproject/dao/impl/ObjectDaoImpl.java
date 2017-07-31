package com.salon.backstage.qcproject.dao.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.qc.dao.BD;
import com.salon.backstage.core.PO;
import com.salon.backstage.qcproject.dao.ObjectDao;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Page;
import com.salon.backstage.qcproject.util.Sql;

/**
 * 作者：齐潮
 * 创建日期：2016年12月26日
 * 类说明：
 */
@Repository
public class ObjectDaoImpl extends BD implements ObjectDao {

	@Override
	public List<Map<String, Object>> getListBySql(String sql, Map<String, Object> params, Integer start, Integer size) {
		return getManyTablesBySql(sql, params, start, size);
	}

	@Override
	public int getObjCountBySql(String sql, Map<String, Object> params) {
		return getCountBySql(sql,params);
	}

	@Override
	public Map<String, Object> getObjectBySql(String sql, Map<String, Object> params) {
		return getManyTablesFirstOneBySql(sql, params);
	}

	@Override
	public String save(Class clas, Map<String, Object> pojo) {
		return savePojoForDefault(clas, pojo);
	}

	@Override
	public int update(Class clas, Map<String, Object> conditions, Map<String, Object> settings) {
		return updatePojoByConditions(clas, conditions, settings);
	}

	@Override
	public int updateById(Class clas, String id, Map<String, Object> settings) {
		return updatePojoById(clas, id, settings);
	}

	@Override
	public int myDelete(Class clas, Map<String, Object> conditions) {
		int result = delete(clas, conditions);
		return result;
	}

	@Override
	public int myDeleteById(Class clas, String id) {
		int result = deleteById(clas, id);
		return result;
	}

	@Override
	public <T> List<T> getPos(Class<T> clas, Map<String, Object> params, Integer start, Integer size,Order... orders) {
		return getPojosListByParams(clas, params, start, size, orders);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int getPosCount(Class clas, Map<String, Object> params) {
		return getPojosCountByParams(clas, params);
	}

	@Override
	public List<Map<String, Object>> getPosForMap(Class clas, Map<String, Object> params, Integer start, Integer size,
			Order... orders) {
		return getPojosListByParamsForMap(clas, params, start, size, orders);
	}

	@Override
	public Code getObjects(String countSql,String selectSql,Map<String,Object> params, int page,int eachRows) {
		int count = getCountBySql(countSql,params);
		Page p = Page.init(page, eachRows, count);
		List<Map<String, Object>> list = getManyTablesBySql(selectSql, params, p.getStartIndex(), p.getEachRows());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("page", p);
		map.put("result", list);
		Code result = Code.init(true,1, null, map);
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String save(Object obj) {
//		String id = savePojo(obj);
		Class clas = obj.getClass();
		Field[] fs = clas.getDeclaredFields();
		
		Map<String,Object> objMap = new HashMap<String,Object>();
		try {
			for(Field f:fs){
				f.setAccessible(true);
				String name = f.getName();
				Object value = f.get(obj);
				if(value!=null)
					objMap.put(name,value);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return save(clas, objMap);
	}

	@Override
	public Map<String, Object> getObjByIdForMap(Class clas, String id) {
		return getPojoByIdForMap(clas, id);
	}

	@Override
	public <T> T getObjById(Class<T> clas, String id) {
		return getPojoById(clas, id);
	}

	@Override
	public <T> T getObjByParams(Class<T> clas, Map<String, Object> params) {
		return getPojoByParams(clas, params);
	}

	@Override
	public void update(Object obj) {
		getTemplate().update(obj);
	}

	@Override
	public int updateBySql(Sql sql) {
		return updateBySql(sql.getSql(), sql.getParams());
	}

	@Override
	public int saveObjects(Class clas,List<Map<String, Object>> objs) {
		return savePojosForDefault(clas, objs);
	}

}
