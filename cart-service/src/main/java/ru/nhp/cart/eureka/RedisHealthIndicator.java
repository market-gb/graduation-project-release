package ru.nhp.cart.eureka;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

@Component
@RequiredArgsConstructor
public class RedisHealthIndicator implements HealthIndicator {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Health health() {
        if (checkRedis()) {
            return new Health.Builder(Status.UP).build();
        } else {
            return new Health.Builder(Status.DOWN).build();
        }
    }

    private Boolean checkRedis() {
        try (JedisPool pool = new JedisPool()) {
            pool.getResource();
        } catch (JedisConnectionException e) {
            return false;
        }
        return true;
    }

//    Работают оба варианта. Не знаю какой лучше.
//
//    private Boolean checkRedis() {
//        return !Objects.requireNonNull(redisTemplate.getConnectionFactory())
//                .getConnection().isClosed();
//    }
}