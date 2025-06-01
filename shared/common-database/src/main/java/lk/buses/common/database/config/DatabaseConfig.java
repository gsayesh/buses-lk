package lk.buses.common.database.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Common database configuration.
 * Note: @EnableJpaAuditing should be added at the application level (in each microservice)
 * to avoid duplicate bean definitions when this common module is imported.
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {
    // Common database configurations can be added here
}