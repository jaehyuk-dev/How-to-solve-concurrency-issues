package com.example.stock.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisLockRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisLockRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean lock(Long lockKey) {
        return redisTemplate.opsForValue().setIfAbsent(generateKey(lockKey) ,"lock", Duration.ofMillis(3_000));
    }

    public Boolean unlock(Long lockKey) {
        return redisTemplate.delete(generateKey(lockKey));
    }

    private String generateKey(Long key) {
        return key.toString();
    }
}
