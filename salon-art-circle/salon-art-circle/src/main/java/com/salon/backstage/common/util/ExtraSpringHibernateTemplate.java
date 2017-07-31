package com.salon.backstage.common.util;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateTemplate;

public interface ExtraSpringHibernateTemplate {

	long count(Class<?> poc) throws DataAccessException;

	<T> List<T> findAll(Class<T> poc) throws DataAccessException;

	// -- 分页

	<T> Paging<T> findPage(Class<?> poc, Order order, int firstResult, int maxResults) throws DataAccessException;

	<T> Paging<T> findPageByCriteria(DetachedCriteria queryListCriteria, final DetachedCriteria queryCountCriteria,
			int firstResult, int maxResults) throws DataAccessException;

	// -- FirstOne

	<T> T findFirstOneByCriteria(DetachedCriteria criteria) throws DataAccessException;

	<T> T findFirstOneByPropEq(Class<?> poc, String propName, Object propValue, Order order) throws DataAccessException;

	<T> T findFirstOneByPropEq(Class<?> poc, String propName, Object propValue) throws DataAccessException;
	public <T> T findFirstOneByPropEq(Class<?> poc, String[] propName, Object[] propValue) throws DataAccessException;
	public <T> T findFirstOneByPropEq(Class<?> poc, String[] propName, Object[] propValue, Order order) throws DataAccessException;
	void deleteFirstOneByPropEq(Class<?> poc, String propName, Object propValue) throws DataAccessException;

	// -- All

	<T> List<T> findAllByCriteria(DetachedCriteria criteria) throws DataAccessException;

	<T> List<T> findAllByPropEq(Class<?> poc, String propName, Object propValue, Order order)
			throws DataAccessException;

	<T> List<T> findAllByPropEq(Class<?> poc, String propName, Object propValue) throws DataAccessException;
	<T> List<T> findAllByPropEq(Class<?> poc, String[] propName, Object[] propValue) throws DataAccessException;

	
	void deleteAllByPropEq(Class<?> poc, String propName, Object propValue) throws DataAccessException;
	
	// -- 依赖注入

	HibernateTemplate getHibernateTemplate();

	void setHibernateTemplate(HibernateTemplate hibernateTemplate);
	 List createSQLQuery(String sql,Class cl);
	 List createSQLQueryFindAll(String sql);
	 List createSQLQueryFindAll(String sql,Class cl);
	 <T> List<T> createSQLQueryFindAll(Class<T> cl,String sql); //新增
	 Map createSQLQueryFindFirstOne(String sql);
	 List createSQLQueryFindPaging(String sql,int firstResult,int maxResults);
	 long createSQLQueryCount(String sql);
	 <T> Paging<T> createSQLQueryfindPage(String sql, int firstResult, int maxResults) throws DataAccessException;

	// -- 查询谓语

	static class C {
		public static DetachedCriteria build(Class<?> clazz, Order order, Criterion criterion) {
			DetachedCriteria detached = DetachedCriteria.forClass(clazz);
			detached.addOrder(order);
			detached.add(criterion);
			return detached;
		}

		public static DetachedCriteria build(Class<?> clazz, Criterion criterion) {
			DetachedCriteria detached = DetachedCriteria.forClass(clazz);
			detached.add(criterion);
			return detached;
		}

		public static DetachedCriteria buildRowCount(Class<?> clazz, Criterion criterion) {
			DetachedCriteria detached = DetachedCriteria.forClass(clazz);
			detached.setProjection(Projections.rowCount());
			detached.add(criterion);
			return detached;
		}
	}

	List createSQLQueryFindPaging(String sql, int firstResult, int maxResults,
			Object[] values);

	List createSQLQueryFindAll(String sql, Object[] values);

	long createSQLQueryCount(String sql, Object[] values);

	<T>Paging<T> createSQLQueryfindPage(String sql, int firstResult,
			int maxResults, Object[] values) throws DataAccessException;

	Map createSQLQueryFindFirstOne(String sql, Object[] values);

	int saveOrUpdate(String hql);


	
}
