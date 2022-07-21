package ru.nhp.cart.eureka;

import com.netflix.appinfo.HealthCheckHandler;
import com.netflix.appinfo.InstanceInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
/*
С помощью классов RedisHealthCheckHandler и RedisHealthIndicator сервис сам может
управлять своим статусом в eureka. Если в сервисе, например, отвалился Redis,
то для eureka он по-прежнему "в строю", а для других сервисов он уже не функционален.
И в этом случае сервис сам меняет свой статус на "Down", а когда соединение с Redis
восстанавливается сервис сам меняет свой статус в eureka на "Up".
*/

@Component
@RequiredArgsConstructor
public class RedisHealthCheckHandler implements HealthCheckHandler {
    private final RedisHealthIndicator redisHealthIndicator;

    @Override
    public InstanceInfo.InstanceStatus getStatus(InstanceInfo.InstanceStatus instanceStatus) {
        Status status = redisHealthIndicator.health().getStatus();
        if (status.equals(Status.UP)) {
            return InstanceInfo.InstanceStatus.UP;
        } else {
            return InstanceInfo.InstanceStatus.DOWN;
        }
    }
}
