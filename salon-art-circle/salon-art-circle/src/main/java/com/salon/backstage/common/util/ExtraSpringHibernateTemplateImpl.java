package com.salon.backstage.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;

public class ExtraSpringHibernateTemplateImpl implements ExtraSpringHibernateTemplate {
	private HibernateTemplate hibernateTemplate;

	@Override
	public long count(final Class<?> poc) throws DataAccessException {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException {
				Criteria executableCriteria = DetachedCriteria.forClass(poc).setProjection(Projections.rowCount())
						.getExecutableCriteria(session);
				return (Long) executableCriteria.uniqueResult();
			}
		});
	}

	@Override
	public <T> List<T> findAll(Class<T> poc) throws DataAccessException {
		return hibernateTemplate.loadAll(poc);
	}

	// -- 分页

	@Override
	@SuppressWarnings("unchecked")
	public <T> Paging<T> findPage(Class<?> poc, Order order, int firstResult, int maxResults)
			throws DataAccessException {
		List<T> list = (List<T>) hibernateTemplate.findByCriteria(DetachedCriteria.forClass(poc).addOrder(order),
				firstResult, maxResults);
		long count = count(poc);
		return new Paging<T>(list, firstResult, maxResults, (int) count);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> Paging<T> findPageByCriteria(DetachedCriteria queryListCriteria,
			final DetachedCriteria queryCountCriteria, int firstResult, int maxResults) throws DataAccessException {
		List<T> list = (List<T>) hibernateTemplate.findByCriteria(queryListCriteria, firstResult, maxResults);
		long count = hibernateTemplate.executeWithNativeSession(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session) throws HibernateException {
				Criteria executableCriteria = queryCountCriteria.getExecutableCriteria(session);
				return (Long) executableCriteria.uniqueResult();
			}
		});
		return new Paging<T>(list, firstResult, maxResults, (int) count);
	}
	
	// -- FirstOne
	@Override
	public <T> T findFirstOneByCriteria(final DetachedCriteria criteria) throws DataAccessException {
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<T>() {
			@Override
			@SuppressWarnings("unchecked")
			public T doInHibernate(Session session) throws HibernateException {
				Criteria executableCriteria = criteria.getExecutableCriteria(session);
				executableCriteria.setMaxResults(1);
				return (T) executableCriteria.uniqueResult();
			}
		});
	}
	
	@Override
	public <T> T findFirstOneByPropEq(Class<?> poc, String propName, Object propValue, Order order)
			throws DataAccessException {
		DetachedCriteria detached = DetachedCriteria.forClass(poc);
		detached.add(Restrictions.eq(propName, propValue));
		if (order != null) {
			detached.addOrder(order);
		}
		return findFirstOneByCriteria(detached);
	}
	@Override
	public <T> T findFirstOneByPropEq(Class<?> poc, String[] propName, Object[] propValue)
			throws DataAccessException {
		DetachedCriteria detached = DetachedCriteria.forClass(poc);
		for(int i=0;i<propName.length;i++){
			detached.add(Restrictions.eq(propName[i], propValue[i]));
		}
		return findFirstOneByCriteria(detached);
	}
	@Override
	public <T> T findFirstOneByPropEq(Class<?> poc, String propName, Object propValue) throws DataAccessException {
		return findFirstOneByPropEq(poc, propName, propValue, null);
	}

	@Override
	public void deleteFirstOneByPropEq(Class<?> poc, String propName, Object propValue) throws DataAccessException {
		hibernateTemplate.delete(findFirstOneByPropEq(poc, propName, propValue));
	}

	// -- All

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findAllByCriteria(DetachedCriteria criteria) throws DataAccessException {
		return (List<T>) hibernateTemplate.findByCriteria(criteria);
	}

	@Override
	public <T> List<T> findAllByPropEq(Class<?> poc, String propName, Object propValue, Order order)
			throws DataAccessException {
		DetachedCriteria detached = DetachedCriteria.forClass(poc);
		detached.add(Restrictions.eq(propName, propValue));
		if (order != null) {
			detached.addOrder(order);
		}
		return findAllByCriteria(detached);
	}

	@Override
	public <T> List<T> findAllByPropEq(Class<?> poc, String propName, Object propValue) throws DataAccessException {
		return findAllByPropEq(poc, propName, propValue, null);
	}

	@Override
	public void deleteAllByPropEq(Class<?> poc, String propName, Object propValue) throws DataAccessException {
		hibernateTemplate.deleteAll(findAllByPropEq(poc, propName, propValue));
	}

	// -- 依赖注入

	@Override
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Override
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	@Override
	public <T> List<T> findAllByPropEq(Class<?> poc, String[] propName,
			Object[] propValue) throws DataAccessException {
		// TODO Auto-generated method stub
		DetachedCriteria detached = DetachedCriteria.forClass(poc);
		for(int i=0;i<propName.length;i++){
			detached.add(Restrictions.eq(propName[i], propValue[i]));
		}
		return findAllByCriteria(detached);
	}

	@Override
	public List createSQLQuery(String sql,Class cl) {
		Session sess= getHibernateTemplate().getSessionFactory().openSession();
		List list = sess.createSQLQuery(sql).addEntity(cl).list();
		sess.close();
		return list;
	}
	@Override
	public List createSQLQueryFindAll(String sql) {
		Session sess= getHibernateTemplate().getSessionFactory().openSession();
		SQLQuery  query=sess.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);	
		List<Map> list=query.list();
		sess.close();
		return list;
	}
	@Override
	public List createSQLQueryFindAll(String sql, Class cl) {
		Session sess= getHibernateTemplate().getSessionFactory().openSession();
		List list = sess.createSQLQuery(sql).addEntity(cl).list();
		sess.close();
		return list;
	}
	@Override
	public <T> List<T> createSQLQueryFindAll(Class<T> cl, String sql) {
		Session sess= getHibernateTemplate().getSessionFactory().openSession();
		SQLQuery sqluery = sess.createSQLQuery(sql).addEntity(cl); 
		List<T> tmpList = sqluery.list();  
		sess.close();
		return tmpList;
	}

	@Override
	public List createSQLQueryFindAll(String sql,Object [] values) {
		Session sess=	getHibernateTemplate().getSessionFactory().openSession();
		SQLQuery  query=sess.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);	
		for(int i=0;i<values.length;i++){
			query.setParameter(i, values[i]);
		}
		List<Map> list=query.list();
		sess.close();
		return list;
	}
	@Override
	public List createSQLQueryFindPaging(String sql,int firstResult,int maxResults) {
		Session sess=	getHibernateTemplate().getSessionFactory().openSession();
		SQLQuery  query=sess.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);	
		query.setFirstResult(firstResult).setMaxResults(maxResults);
		List<Map> list=query.list();
		sess.close();
		return list;
	}
	@Override
	public List createSQLQueryFindPaging(String sql,int firstResult,int maxResults,Object [] values) {
		Session sess=	getHibernateTemplate().getSessionFactory().openSession();
		SQLQuery  query=sess.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);	
		query.setFirstResult(firstResult).setMaxResults(maxResults);
		for(int i=0;i<values.length;i++){
			query.setParameter(i, values[i]);
		}
		List<Map> list=query.list();
		sess.close();
		return list;
	}

	@Override
	public long createSQLQueryCount(String sql) {
		Session sess=	getHibernateTemplate().getSessionFactory().openSession();
		SQLQuery  query=sess.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);	
		int count=query.list().size();
		sess.close();
		return count;
	}
	@Override
	public int saveOrUpdate(String sql) {
		Session session=	getHibernateTemplate().getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		Query queryupdate=session.createSQLQuery(sql);
		int ret=queryupdate.executeUpdate();
		trans.commit();
		session.close();
		return ret;
	}
	@Override
	public long createSQLQueryCount(String sql,Object [] values) {
		Session sess=	getHibernateTemplate().getSessionFactory().openSession();
		SQLQuery  query=sess.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);	
		for(int i=0;i<values.length;i++){
			query.setParameter(i, values[i]);
		}
		int count=query.list().size();
		sess.close();
		return count;
	}
	@Override
	public <T> Paging<T> createSQLQueryfindPage(String sql, int firstResult,
			int maxResults) throws DataAccessException {
		long count=createSQLQueryCount(sql);
		List list=createSQLQueryFindPaging(sql,firstResult,maxResults);
		return new Paging<T>(list, firstResult, maxResults, (int) count);
	}
	@Override
	public <T> Paging<T> createSQLQueryfindPage(String sql, int firstResult, int maxResults,Object[] values) throws DataAccessException {
		long count=createSQLQueryCount(sql,values);
		List list=createSQLQueryFindPaging(sql,firstResult,maxResults,values);
		return new Paging<T>(list, firstResult, maxResults, (int) count);
	}
	@Override
	public Map createSQLQueryFindFirstOne(String sql) {
		Session sess=	getHibernateTemplate().getSessionFactory().openSession();
		SQLQuery  query=sess.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		Map map = null;
		List list = query.list();
		if(list == null || "[]".equals(list.toString())){
			return map;
		}
		map = (Map)query.list().get(0);
		sess.close();
		return map;
	}
	@Override
	public Map createSQLQueryFindFirstOne(String sql,Object [] values) {
		Session sess=	getHibernateTemplate().getSessionFactory().openSession();
		SQLQuery  query=sess.createSQLQuery(sql);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);	
		query.setFirstResult(0).setMaxResults(1);
		for(int i=0;i<values.length;i++){
			query.setParameter(i, values[i]);
		}
		Map map=new HashMap();
		List<Map> list=query.list();
		if(list.size()!=0){
			 map=list.get(0);
		}
		sess.close();
		return map;
	}

	@Override
	public <T> T findFirstOneByPropEq(Class<?> poc, String[] propName,
			Object[] propValue, Order order) throws DataAccessException {
		DetachedCriteria detached = DetachedCriteria.forClass(poc);
		for(int i=0;i<propName.length;i++){
			detached.add(Restrictions.eq(propName[i], propValue[i]));
		}
		if (order != null) {
			detached.addOrder(order);
		}
		return findFirstOneByCriteria(detached);
	}
}

