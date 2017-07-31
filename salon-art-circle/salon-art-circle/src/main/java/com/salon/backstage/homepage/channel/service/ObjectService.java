package com.salon.backstage.homepage.channel.service;
/**
 * 作者：齐潮
 * 创建日期：2016年12月17日
 * 类说明：处理一些公用的信息
 */
public interface ObjectService {

	/**
	 * 新增一条数据
	 */
	public String save(Object obj);
	
	/**
	 * 条件获取一条信息
	 * @param clas
	 * @param proNames
	 * @param values
	 * @return
	 */
	public <T> T get(Class<T> clas,String[] proNames,Object[] values);
	
}
