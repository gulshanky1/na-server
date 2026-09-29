package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final StringRedisTemplate redisTemplate;

    private static final String RATE_LIMIT_SCRIPT = """
            local current = redis.call('INCR', KEYS[1])
            if current == 1 then
                redis.call('EXPIRE', KEYS[1], ARGV[1])
            end
            return current
            """;

    private final DefaultRedisScript<Long> script =
            new DefaultRedisScript<>(RATE_LIMIT_SCRIPT, Long.class);

    public boolean allowed(String key, int limit, long windowSeconds) {

        Long count = redisTemplate.execute(
                script,
                List.of(key),
                String.valueOf(windowSeconds)
        );

        return count != null && count <= limit;
    }
}