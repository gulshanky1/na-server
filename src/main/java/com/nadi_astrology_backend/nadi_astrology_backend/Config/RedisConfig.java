package com.nadi_astrology_backend.nadi_astrology_backend.Config;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.UserResponse;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig implements CachingConfigurer {

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {

        JacksonJsonRedisSerializer<UserResponse> userResponseSerializer =
                new JacksonJsonRedisSerializer<>(UserResponse.class);

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(userResponseSerializer)
                );
    }

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connectionFactory,
            RedisCacheConfiguration redisCacheConfiguration) {

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new RedisCacheErrorHandler();
    }
}