package com.example.proxy_server.service.impl;

import com.example.proxy_server.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    private final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    private transient final RedisTemplate<String , String> redisTemplate;

    private transient final HashOperations<String, Long, String > hashOperations;

    private transient final ValueOperations<String , String > valueOperations;

    private final ObjectMapper objectMapper;

    RedisServiceImpl(RedisTemplate<String, String> redisTemplate,
                     ObjectMapper objectMapper){
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.valueOperations = redisTemplate.opsForValue();
        this.objectMapper = objectMapper;
    }

    @Value("${CACHE_KEY:CACHE_KEY}")
    private String CACHE_KEY;

    @Value("${RESPONSE_TTL:1}")
    private Long RESPONSE_TTL;

    @Override
    public void save(String key, Object val) {
        try{
            String object = objectMapper.writeValueAsString(val);
            valueOperations.set(CACHE_KEY+key, object, RESPONSE_TTL, TimeUnit.DAYS);
            logger.info("Response saved successfully...");
        }catch (Exception e){
            logger.info("Error while saving response in cache {}", e.getMessage(), e);
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        if (key == null || key.isEmpty()){
            logger.info("Key cannot be empty or null");
            return null;
        }
        try {
            String json = valueOperations.get(CACHE_KEY + key);
            if (json == null){
                logger.info("Cache Miss");
                return null;
            }
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.info("Error while fetching the response {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void clearAll() {

        RedisConnectionFactory redisConnectionFactory = redisTemplate.getConnectionFactory();

        if (redisConnectionFactory != null){
            redisConnectionFactory.getConnection().flushAll();
        }

        logger.info("Redis connection factory is null : {}", redisConnectionFactory);
    }
}
