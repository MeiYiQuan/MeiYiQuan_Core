package com.salon.backstage.common.util;

import java.util.Map;

/**
 * 作者：齐潮
 * 创建日期：2016年12月19日
 * 类说明：用于对传递参数有效性的验证
 */
public class Validate {

	/**
	 * 可以验证map中的keys是否为空值，如果全部不为空，则返回true，有空值就返回false
	 * @param map
	 * @param keys
	 * @return
	 */
	public static boolean validate(Map<String,Object> map,String... keys){
		if(map==null)
			return false;
		if(keys==null||keys.length<1)
			return true;
		for(String key:keys){
			Object valueObj = map.get(key);
			if(valueObj==null||valueObj.toString().trim().equals(""))
				return false;
		}
		return true;
	}
	
}
