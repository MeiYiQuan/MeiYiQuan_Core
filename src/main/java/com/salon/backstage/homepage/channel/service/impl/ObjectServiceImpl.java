package com.salon.backstage.homepage.channel.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.homepage.channel.service.ObjectService;

/**
 * 作者：齐潮
 * 创建日期：2016年12月17日
 * 类说明：
 */
@Service
public class ObjectServiceImpl implements ObjectService {

	@Autowired
	private ExtraSpringHibernateTemplate template;
	
	@Override
	public String save(Object obj) {
		Serializable serId = template.getHibernateTemplate().save(obj);
		if(serId==null)
			return null;
		return serId.toString();
	}

	@Override
	public <T> T get(Class<T> clas, String[] proNames, Object[] values) {
		T t = template.findFirstOneByPropEq(clas, proNames, values);
		return t;
	}

}
