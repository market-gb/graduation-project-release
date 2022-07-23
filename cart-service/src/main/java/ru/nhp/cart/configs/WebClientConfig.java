package ru.nhp.cart.configs;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import ru.nhp.cart.properties.CoreServiceIntegrationProperties;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties({
        CoreServiceIntegrationProperties.class,
})
@RequiredArgsConstructor
public class WebClientConfig {
    private final CoreServiceIntegrationProperties coreServiceIntegrationProperties;

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientWithTimeout() {

        // WebClient работает с eureka только если bean возвращает builder

        HttpClient httpClient = reactor.netty.http.client.HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, coreServiceIntegrationProperties.getConnectTimeout())
                .responseTimeout(Duration.ofMillis(coreServiceIntegrationProperties.getResponseTimeout()))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(coreServiceIntegrationProperties.getReadTimeout(), TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(coreServiceIntegrationProperties.getWriteTimeout(), TimeUnit.MILLISECONDS)));
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
