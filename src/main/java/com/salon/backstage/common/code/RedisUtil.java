package com.salon.backstage.common.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisUtil {

	// 操作redis客户端
	private static Jedis jedis;
	@Autowired
	private static JedisConnectionFactory jedisConnectionFactory;

	/**
	 * 获取一个jedis 客户端
	 * @return
	 */
	public static Jedis getJedis() {
		if (jedis == null) {
			return jedisConnectionFactory.getShardInfo().createResource();
		}
		return jedis;
	}
	
	/**
	 * 设置Hash类型的值
	 * @param hashName
	 * @param key
	 * @param value
	 */
	public static void setHashValue(String hashName, String key, String value){
		Map<String, String> map = new HashMap<String, String>();
		map.put(key, value);
		getJedis().hmset(hashName, map);
	}
	
	/**
	 * 获取Hash类型的值
	 * @param hashName
	 * @param key
	 * @return
	 */
	public static String getHashValue(String hashName, String key){
		List<String> value = getJedis().hmget(hashName, key);
		return value.get(0);
	}
}
