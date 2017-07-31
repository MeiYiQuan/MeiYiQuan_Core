package com.salon.backstage.common.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis client ops
 * @author wangz
 *
 */
public interface RedisClient {
	
	void set(String key , Object value);
	void set(String key , Object value , long timeout, TimeUnit timeUnit);
	void setJSON(String key , Object value, long timeout, TimeUnit timeUnit);
	void setHash(String hKey, String key , String value);
	void setHash(String hKey, String key , String value, long timeout, TimeUnit timeUnit);
	void setHashJSON(String hKey, String key , Object value);
	void setHashJSON(String hKey, String key , Object value, long timeout, TimeUnit timeUnit);
	String get(String key);
	<T> T get(String key , Class<T> clazz);
	<T> T getJSON(String key, Class<T> clazz);
	<T> T getHashJSON(String hKey, String key , Class<T> clazz);
	String getHash(String hKey , String key);
	void del(String key);
	boolean exists(String key);
	void del(List<String> keys);
	long incr(String key);
	long ttl(String key);
	boolean expire(String key, long seconds);
	long decr(String key);
}
