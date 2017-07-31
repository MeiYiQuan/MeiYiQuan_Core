package com.salon.backstage.homepage.statistics.service;

import com.salon.backstage.pub.bsc.dao.po.Statistics;


/**
 * 
 */
public interface IStatisticsService {

	/**
	 * 统计加一，不用考虑是否存在Statistics
	 * @param id
	 * @param dataName
	 * @param type
	 * @return
	 */
	public boolean addStatistics(String dataName,int type,String... ids);
	
	/**
	 * 统计减一，如果返回false，说明是不合理的，外界可以直接提示系统异常
	 * @param id
	 * @param dataName
	 * @param type
	 * @return
	 */
	public	boolean subStatistics(String dataName,int type,String... ids);
	
	/**
	 * 获得统计对象，如果不存在则直接创建，返回null的话可以直接提示异常
	 * @param id
	 * @param type
	 * @return
	 */
	public Statistics getStatistics(String id,int type);
	
	/*
	//创建统计
	public Statistics saveStatistics(String id);
	
	//创建统计
	public Statistics saveStatisticsByType(String id,String type);
	*/
	
}
