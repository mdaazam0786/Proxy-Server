package com.example.proxy_server.config;

import com.example.proxy_server.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@EnableCaching
@Configuration
public class RedisConfig {

    @Value("${redis.max.idle}")
    private int redisMaxIdle;

    @Value("${redis.min.idle}")
    private int redisMinIdle;

    @Value("${redis.maxTotal}")
    private int redisMaxTotal;

    @Value("${redis.pool.max.wait}")
    private long redisPoolMaxWaitMillis;

    @Value("${redis.pool.min.evictable.millis}")
    private long redisPoolMinEvictableMillis;

    @Value("${min.evictable.idle.duration}")
    private long minEvictableIdleDuration;

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.database}")
    private int redisDatabase;

    @Value("${redis.connection.timeout}")
    private int redisConnectionTimeout;

    @Bean
    JedisConnectionFactory jedisConnectionFactory(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisMaxIdle);
        jedisPoolConfig.setMaxTotal(redisMaxTotal);
        jedisPoolConfig.setMinIdle(redisMinIdle);
        jedisPoolConfig.setMaxWaitMillis(redisPoolMaxWaitMillis);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(redisPoolMinEvictableMillis);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleDuration);

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();

        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(redisPort);
        jedisConnectionFactory.setDatabase(redisDatabase);
        jedisConnectionFactory.setTimeout(redisConnectionTimeout);
        jedisConnectionFactory.setClientName("proxy-server");

        return jedisConnectionFactory;

    }

    @Primary
    @Bean
    public RedisTemplate<String , String> redisTemplate(){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<String>(String.class));
        redisTemplate.setValueSerializer(new GenericToStringSerializer<String>(String.class));
        return redisTemplate;
    }
}
