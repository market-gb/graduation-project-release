package user.eureka;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import user.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class UserHealthIndicator implements HealthIndicator {
    private final UserRepository userRepository;

    @Override
    public Health health() {
        if (checkDB()) {
            return new Health.Builder(Status.UP).build();
        } else {
            return new Health.Builder(Status.DOWN).build();
        }
    }

    private Boolean checkDB() {
        try {
            userRepository.count();
        } catch (Exception exception) {
            return false;
        }
        return true;
    }
}