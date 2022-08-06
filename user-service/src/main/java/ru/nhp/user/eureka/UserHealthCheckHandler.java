package ru.nhp.user.eureka;

import com.netflix.appinfo.HealthCheckHandler;
import com.netflix.appinfo.InstanceInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
/*
С помощью классов UserHealthCheckHandler и UserHealthIndicator сервис сам может
управлять своим статусом в eureka. Если в сервисе, например, упала БД,
то для eureka он по-прежнему "в строю", а для других сервисов он уже не функционален.
И в этом случае сервис сам меняет свой статус на "Down", а когда соединение с БД
восстанавливается он сам меняет свой статус в eureka на "Up".
*/

@Component
@RequiredArgsConstructor
public class UserHealthCheckHandler implements HealthCheckHandler {
    private final UserHealthIndicator userHealthIndicator;

    @Override
    public InstanceInfo.InstanceStatus getStatus(InstanceInfo.InstanceStatus instanceStatus) {
        Status status = userHealthIndicator.health().getStatus();
        if (status.equals(Status.UP)) {
            return InstanceInfo.InstanceStatus.UP;
        } else {
            return InstanceInfo.InstanceStatus.DOWN;
        }
    }
}
