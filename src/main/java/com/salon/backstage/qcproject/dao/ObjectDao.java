package com.salon.backstage.qcproject.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;

import com.salon.backstage.core.PO;
import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Sql;

/**
 * 作者：齐潮
 * 创建日期：2016年12月26日
 * 类说明：处理数据库操作
 */
public interface ObjectDao {

	/**
	 * 通过sql语句和参数集合来分页获取信息
	 * @param sql
	 * @param params
	 * @param start
	 * @param size
	 * @return
	 */
	public List<Map<String,Object>> getListBySql(String sql,Map<String,Object> params,Integer start,Integer size);
	
	/**
	 * 条件获取条数
	 * @param sql
	 * @param params
	 * @return
	 */
	public int getObjCountBySql(String sql,Map<String,Object> params);
	
	/**
	 * 条件分页获取PO集合
	 * @param <T>
	 * @param clas
	 * @param params
	 * @param start
	 * @param size
	 * @return
	 */
	public <T> List<T> getPos(Class<T> clas,Map<String,Object> params,Integer start,Integer size,Order... orders);
	
	/**
	 * 条件分页获取PO集合,用map来表示po
	 * @param clas
	 * @param params
	 * @param start
	 * @param size
	 * @param orders
	 * @return
	 */
	public List<Map<String,Object>> getPosForMap(Class clas,Map<String,Object> params,Integer start,Integer size,Order... orders);
	
	/**
	 * 条件获取po个数
	 * @param clas
	 * @param params
	 * @return
	 */
	public int getPosCount(Class clas,Map<String,Object> params);
	
	/**
	 * 条件获取一个对象
	 * @param sql
	 * @param params
	 * @return
	 */
	public Map<String,Object> getObjectBySql(String sql,Map<String,Object> params);
	
	/**
	 * 添加一条信息
	 * @param clas
	 * @param pojo
	 * @return
	 */
	public String save(Class clas,Map<String,Object> pojo);
	
	/**
	 * 添加一条信息
	 * @param obj
	 * @return
	 */
	public String save(Object obj);
	
	/**
	 * 更新一条信息
	 * @param clas
	 * @param conditions
	 * @param settings
	 * @return
	 */
	public int update(Class clas,Map<String,Object> conditions,Map<String,Object> settings);
	
	/**
	 * 通过id去更新信息
	 * @param clas
	 * @param id
	 * @param settings
	 * @return
	 */
	public int updateById(Class clas,String id,Map<String,Object> settings);
	
	/**
	 * 通过条件去删除信息
	 * @param clas
	 * @param conditions
	 * @return
	 */
	public int myDelete(Class clas,Map<String,Object> conditions);
	
	/**
	 * 通过id去删除信息
	 * @param clas
	 * @param id
	 * @return
	 */
	public int myDeleteById(Class clas,String id);
	
	/**
	 * 通过id去获取一个对象，结果用map表示
	 * @param clas
	 * @param id
	 * @return
	 */
	public Map<String,Object> getObjByIdForMap(Class clas,String id);
	
	/**
	 * 通过id去获取一个对象
	 * @param clas
	 * @param id
	 * @return
	 */
	public <T> T getObjById(Class<T> clas,String id);
	
	/**
	 * 条件获取一个对象，只取第一个
	 * @param clas
	 * @param params
	 * @return
	 */
	public <T> T getObjByParams(Class<T> clas,Map<String,Object> params);
	
	/**
	 * 更新一个对象(用的是hibernate的处理方式，没有返回值)
	 * @param obj
	 * @return
	 */
	public void update(Object obj);
	
	/**
	 * 通过sql语句来更新信息
	 * @param sql
	 * @return
	 */
	public int updateBySql(Sql sql);
	
	/**
	 * 批量新增信息，并返回新增的条数
	 * @param clas
	 * @param objs
	 * @return
	 */
	public int saveObjects(Class clas,List<Map<String,Object>> objs);
	

	
	// ---------------------------------------------------------
	
	/**
	 * 条件分页去获取集合
	 * @param sql
	 * @param params
	 * @param page
	 * @return
	 */
	public Code getObjects(String countSql,String selectSql,Map<String,Object> params, int page,int eachRows);
}
