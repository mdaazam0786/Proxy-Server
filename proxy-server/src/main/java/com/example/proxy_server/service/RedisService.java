package com.example.proxy_server.service;

import org.springframework.stereotype.Service;

public interface RedisService {

    void save(String key , Object val);

    <T> T get(String key, Class<T> clazz);

    void clearAll();

}
