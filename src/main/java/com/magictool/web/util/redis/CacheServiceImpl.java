package com.magictool.web.util.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author linxinze
 *
 */
public class CacheServiceImpl implements CacheService {

	private final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

	private final StringRedisTemplate redisTemplate;

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public CacheServiceImpl(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public long increment(String cacheKey, long expiredTime) {
		long count = Objects.requireNonNull(redisTemplate.opsForValue().increment(cacheKey, 1));
		if (count == 1) {
			//设置有效期
			redisTemplate.expire(cacheKey, expiredTime, TimeUnit.MILLISECONDS);
		}
		return count;
	}

	@Override
	public long increment(String cacheKey, long delta, long expiredTime) {
		if(delta < 0){
			logger.error("delta must be greater than 0");
			return 0;
		}
		long count = Objects.requireNonNull(redisTemplate.opsForValue().increment(cacheKey, delta));
		if (expiredTime > 0 ){
			redisTemplate.expire(cacheKey, expiredTime, TimeUnit.MILLISECONDS);
		}
		return count;
	}

	@Override
	public long decrement(String cacheKey) {
		return Objects.requireNonNull(redisTemplate.opsForValue().increment(cacheKey, -1));
	}

	@Override
	public <T> void put(String cacheKey, T data) {
		if (StringUtils.isBlank(cacheKey)) {
			logger.debug("sorry your cache key is null ，then return now  !!!!!");
			return;
		}
		try {
			String json = OBJECT_MAPPER.writeValueAsString(data);
			redisTemplate.opsForValue().set(cacheKey, json);
		} catch (JsonProcessingException e) {
			logger.error("failed to convert the cache value to json", e);
		}

	}

	@Override
	public <T> void put(String cacheKey, T data, long expiredTime) {
		if (StringUtils.isBlank(cacheKey) || expiredTime == 0) {
			logger.debug("sorry your cache key is null or expired time is 0 ，then return now  !!!!!");
			return;
		}
		try {
			String json = OBJECT_MAPPER.writeValueAsString(data);
			redisTemplate.opsForValue().set(cacheKey, json);
			redisTemplate.expire(cacheKey, expiredTime, TimeUnit.MILLISECONDS);
		} catch (JsonProcessingException e) {
			logger.error("failed to convert the cache value to json", e);
		}

	}

	@Override
	public <T> void refresh(String cacheKey, long expiredTime) {
		if (StringUtils.isBlank(cacheKey) || expiredTime == 0) {
			logger.debug("sorry your cache key is null or expired time is 0 ，then return now  !!!!!");
			return;
		}
		redisTemplate.expire(cacheKey, expiredTime, TimeUnit.MILLISECONDS);
	}

	@Override
	public <T> T get(String key, Class<T> clazzT) {
		String value = getValueByKey(key);
		if (value == null) {
			return null;
		}
		T data = null;
		try {
			data = OBJECT_MAPPER.readValue(value, clazzT);
		} catch (IOException e) {
			logger.error("failed to convert the cache value to object['" + clazzT + "']", e);
		}
		return data;
	}

	@Override
	public <T> T get(String key, TypeReference<T> typeReference) {
		String value = getValueByKey(key);
		if (value == null) {
			return null;
		}
		T data = null;
		try {
			data = OBJECT_MAPPER.readValue(value, typeReference);
		} catch (IOException e) {
			logger.error("failed to convert the cache value to object['" + typeReference + "']", e);
		}
		return data;
	}

	private String getValueByKey(String key) {
		if (StringUtils.isBlank(key)) {
			logger.debug("sorry your cache key is null , then return null now !!!!!");
			return null;
		}
		String value = redisTemplate.opsForValue().get(key);
		if (StringUtils.isBlank(value)) {
			logger.debug("sorry your cache key fetch the value failed return null now !!!!!");
			return null;
		}
		return value;
	}

	@Override
	public String getAsString(String cacheKey) {
		if (StringUtils.isBlank(cacheKey)) {
			logger.debug("sorry your cache key is null,then return null now !!!!!");
			return null;
		}
		return redisTemplate.opsForValue().get(cacheKey);
	}

	@Override
	public void put(String cacheKey, String cacheValue) {
		if (StringUtils.isBlank(cacheKey) || StringUtils.isBlank(cacheValue)) {
			logger.debug("sorry your cache key or cache value is null , then this method is exit now !!!!!");
			return;
		}
		redisTemplate.opsForValue().set(cacheKey, cacheValue);
	}

	@Override
	public void put(String cacheKey, String cacheValue, long expiredTime) {
		if (StringUtils.isBlank(cacheKey) || StringUtils.isBlank(cacheValue)) {
			logger.debug("sorry your cache key or cache value is null , then this method is exit now !!!!!");
			return;
		}
		redisTemplate.opsForValue().set(cacheKey, cacheValue);
		redisTemplate.expire(cacheKey, expiredTime, TimeUnit.MILLISECONDS);
	}

	@Override
	public void delete(String cacheKey) {
		if (StringUtils.isBlank(cacheKey)) {
			logger.debug("sorry your cache key is null,then this method is exit now");
			return;
		}
		if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
			redisTemplate.delete(cacheKey);
		}
	}

	@Override
	public Set<String> getPatternKeys(String keyPattern) {
		if (StringUtils.isBlank(keyPattern)) {
			logger.debug("sorry your cache key pattern is null,then this method is exit now");
			return null;
		}
		return redisTemplate.keys(keyPattern);
	}

	@Override
	public void lPush(String cacheKey, String cacheValue) {
		if (StringUtils.isBlank(cacheKey) || StringUtils.isBlank(cacheValue)) {
			logger.debug("sorry your cache key or cache value is null , then this method is exit now !!!!!");
			return;
		}
		redisTemplate.opsForList().leftPush(cacheKey, cacheValue);
	}

	@Override
	public String rpop(String cacheKey) {
		if (StringUtils.isBlank(cacheKey)) {
			logger.debug("sorry your cache key is null,then return null now !!!!!");
			return null;
		}
		return redisTemplate.opsForList().rightPop(cacheKey);
	}

	@Override
	public Long queueSize(String cacheKey) {
		if (StringUtils.isBlank(cacheKey)) {
			logger.debug("sorry your cache key is null,then return null now !!!!!");
			return null;
		}

		return redisTemplate.boundListOps(cacheKey).size();
	}

	@Override
	public <T> void lPushData(String cacheKey, T data) {
		if (StringUtils.isBlank(cacheKey)) {
			logger.debug("sorry your cache key is null then return now  !!!!!");
			return;
		}
		try {
			String json = OBJECT_MAPPER.writeValueAsString(data);
			redisTemplate.opsForList().leftPush(cacheKey, json);
		} catch (JsonProcessingException e) {
			logger.error("failed to convert the cache value to json", e);
		}
	}

	@Override
	public <T> T rpopData(String key, Class<T> clazzT) {
		String value = redisTemplate.opsForList().rightPop(key);
		if (value == null) {
			return null;
		}
		T data = null;
		try {
			data = OBJECT_MAPPER.readValue(value, clazzT);
		} catch (IOException e) {
			logger.error("failed to convert the cache value to object['" + clazzT + "']", e);
		}
		return data;
	}


	@Override
	public List<String> getStringList(String key) {
		return redisTemplate.opsForList().range(key, 0, -1);
	}

	@Override
	public void pushStringList(String key, List<String> data) {
		if (data != null && !data.isEmpty()) {
			for (String value : data) {
				lPush(key,value);
			}
		}
	}

	@Override
	public Boolean isPermissionInfoAlreadyPush(String cacheKey,Object value, long time) {
		String asString = getAsString(cacheKey);
		if (StringUtils.isBlank(asString)) {
			put(cacheKey,value,time);
			return false;
		}
		return true;
	}

}
