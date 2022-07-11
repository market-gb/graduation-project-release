package ru.nhp.core.eureka;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.nhp.core.repositories.ProductRepository;

@Component
@RequiredArgsConstructor
public class ProductHealthIndicator implements HealthIndicator {
    private final ProductRepository productRepository;

    @Override
    public Health health() {
        if (checkDB()) {
            return new Health.Builder(Status.UP).build();
        } else {
            return new Health.Builder(Status.DOWN).build();
        }
    }

    private Boolean checkDB(){
        try{
            productRepository.count();
        }catch (Exception exception){
           return false;
        }
        return true;
    }
}