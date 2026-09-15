package com.nadi_astrology_backend.nadi_astrology_backend.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(
            RuntimeException exception,
            Cache cache,
            Object key) {

        log.warn(
                "Redis GET failed for cache '{}' and key '{}'. Falling back to database.",
                cache.getName(),
                key,
                exception
        );
    }

    @Override
    public void handleCachePutError(
            RuntimeException exception,
            Cache cache,
            Object key,
            Object value) {

        log.warn(
                "Redis PUT failed for cache '{}' and key '{}'.",
                cache.getName(),
                key,
                exception
        );
    }

    @Override
    public void handleCacheEvictError(
            RuntimeException exception,
            Cache cache,
            Object key) {

        log.warn(
                "Redis EVICT failed for cache '{}' and key '{}'.",
                cache.getName(),
                key,
                exception
        );
    }

    @Override
    public void handleCacheClearError(
            RuntimeException exception,
            Cache cache) {

        log.warn(
                "Redis CLEAR failed for cache '{}'.",
                cache.getName(),
                exception
        );
    }
}