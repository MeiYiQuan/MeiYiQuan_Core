package com.salon.backstage.qcproject.util;

import java.util.Map;

/**
 * 作者：齐潮
 * 创建日期：2016年12月28日
 * 类说明：负责将传入的集合去除某些字段
 */
public class CleanUtil {

	/**
	 * 去除map中指定的字段
	 * @param map
	 * @param keys
	 */
	public final static void cleanMap(Map<String,Object> map,String... keys){
		if(map!=null){
			for(String key:keys){
				map.remove(key);
			}
		}
	}
	
	/**
	 * 去除source中的特殊字符，如微信中username里加的图标
	 * @param source
	 * @param slipStr
	 * @return
	 */
	public static String filterEmoji(String source,String slipStr) {
        if(source!=null&&!source.trim().equals("")){
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", slipStr);
        }else{
            return source;
        }
    }
	
}
