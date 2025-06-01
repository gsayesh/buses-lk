package lk.buses.gateway.config;

import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.time.Duration;

@Configuration
@ConditionalOnProperty(value = "spring.redis.enabled", matchIfMissing = true)
public class RedisConfig {

    @Bean(destroyMethod = "shutdown")
    public ClientResources clientResources() {
        return DefaultClientResources.create();
    }

    @Bean
    public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer() {
        return clientConfigurationBuilder -> {
            clientConfigurationBuilder
                    .commandTimeout(Duration.ofSeconds(2))
                    .shutdownTimeout(Duration.ofMillis(100));

            clientConfigurationBuilder.clientOptions(ClientOptions.builder()
                    .socketOptions(SocketOptions.builder()
                            .connectTimeout(Duration.ofSeconds(2))
                            .keepAlive(true)
                            .build())
                    .timeoutOptions(TimeoutOptions.enabled())
                    .build());
        };
    }
}