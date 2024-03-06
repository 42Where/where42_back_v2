package kr.where.backend.redis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {
	private final RedisTemplate<String, Object> redisTemplate;

	public void setValues(String key, String data) {
		ValueOperations<String, Object> values = redisTemplate.opsForValue();
		values.set(key, data);
	}

	public void setValues(String key, String data, Duration duration) {
		ValueOperations<String, Object> values = redisTemplate.opsForValue();
		values.set(key, data, duration);
	}

	@Transactional(readOnly = true)
	public String getValues(String key) {
		ValueOperations<String, Object> values = redisTemplate.opsForValue();

		Object object = values.get(key);
		if (object == null) {
			return "false";
		}

		return (String)object;
	}

	public void deleteValues(String key) {
		redisTemplate.delete(key);
	}

	public void expireValues(String key, int timeout) {
		redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
	}

	public void setListOps(String key, List<Object> data) {
		ListOperations<String, Object> values = redisTemplate.opsForList();
		values.rightPushAll(key, data);
	}

	@Transactional(readOnly = true)
	public List<Object> getListOps(String key) {
		Long len = redisTemplate.opsForList().size(key);
		return len == 0 ? new ArrayList<>() : redisTemplate.opsForList().range(key, 0, len - 1);
	}

	public boolean checkExistValue(String value) {
		return !value.equals("false");
	}
}
