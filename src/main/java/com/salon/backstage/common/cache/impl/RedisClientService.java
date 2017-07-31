package com.salon.backstage.common.cache.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.cache.RedisClient;

/**
 * spring-redis 操作
 * @author wangz 
 *
 */
@Service
public class RedisClientService implements RedisClient {
	@Autowired
	public RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	public StringRedisTemplate stringRedisTemplate;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public void del(final String key) {
		if(exists(key)) {
			redisTemplate.delete(key);
		}
	}

	@Override
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	@Override
	public void del(List<String> keys) {
		redisTemplate.delete(keys);
	}
	
	protected byte[] serializeKey(final String key) {
		return redisTemplate.getStringSerializer().serialize(key);
	}
	@SuppressWarnings({ "unchecked" })
	protected byte[] serializeValue(final Object value) {
		RedisSerializer<Object> reidsSerializer = (RedisSerializer<Object>) redisTemplate
				.getValueSerializer();
		return reidsSerializer.serialize(value);
	}

	protected Object deserializeValue(final byte[] value) {
		return redisTemplate.getValueSerializer().deserialize(value);
	}

	@Override
	public void set(String key, Object value) {
		if(exists(key)) {
			del(key);
		}
		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
		if(exists(key)) {
			del(key);
		}
		redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
	}

	@Override
	public void setJSON(String key, Object value, long timeout, TimeUnit timeUnit) {
		if(exists(key)) {
			del(key);
		}
		try {
			String valStr = mapper.writeValueAsString(value);
			redisTemplate.opsForValue().set(key, valStr, timeout, timeUnit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setHash(String hKey, String key, String value) {
		this.redisTemplate.opsForHash().put(hKey, key, value);
	}

	@Override
	public void setHash(String hKey, String key, String value, long timeout, TimeUnit timeUnit) {
		this.redisTemplate.opsForHash().put(hKey, key, value);
		
	}

	@Override
	public String get(String key) {
		return (String) redisTemplate.opsForValue().get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(final String key, final Class<T> clazz) {
		// TODO Auto-generated method stub
		/*return (T) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte [] rawKey = serializeKey(key);
				byte [] rawVal = connection.get(rawKey);
				deserializeValue(rawVal);
			}
		});*/
		return (T) redisTemplate.opsForValue().get(key);
	}

	@Override
	public <T> T getJSON(String key, Class<T> clazz) {
		Object obj = redisTemplate.opsForValue().get(key);
		if(obj == null) {
			return null;
		}
		String strVal = obj.toString();
		try {
			return mapper.readValue(strVal, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setHashJSON(String hKey, String key, Object value) {
		try {
			String strVal = mapper.writeValueAsString(value);
			redisTemplate.opsForHash().put(hKey, key, strVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void setHashJSON(String hKey, String key, Object value, long timeout, TimeUnit timeUnit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T getHashJSON(String hKey, String key, Class<T> clazz) {
		String strVal = redisTemplate.opsForHash().get(hKey, key).toString();
		try {
			return mapper.readValue(strVal, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getHash(String hKey, String key) {
		Object obj = redisTemplate.opsForHash().get(hKey, key);
		if(obj==null)
			return null;
		return obj.toString();
	}
	/**
	 * 原子性递增
	 * @param key
	 * @return
	 */
	@Override
	public long incr(String key) {
		if(exists(key)) {
			redisTemplate.opsForValue().increment(key, 1);
			return Long.parseLong(redisTemplate.opsForValue().get(key).toString());
			
		}
		return 0;
	}
	/**
	 * 获取某key剩余的过期时间
	 * @param key
	 * @return
	 */
	@Override
	public long ttl(final String key) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte [] rawKey = serializeKey(key);
				return connection.ttl(rawKey);
			}
		});
	}
	/**
	 * 设置key的失效时间
	 * @param key
	 * @param seconds
	 * @return
	 */
	@Override
	public boolean expire(final String key, final long seconds) {
		// TODO Auto-generated method stub
		
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte [] rawKey = serializeKey(key);
				return connection.expire(rawKey, seconds);
			}
		});
	}
	/**
	 * 原子性递减
	 * @param key
	 * @return
	 */
	@Override
	public long decr(final String key) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte [] rawKey = serializeKey(key);
				return connection.decr(rawKey);
			}
		});
	}
}
