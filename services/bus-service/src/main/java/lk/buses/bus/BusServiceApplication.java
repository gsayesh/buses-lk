package lk.buses.bus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"lk.buses.bus", "lk.buses.common"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
@EnableScheduling
public class BusServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusServiceApplication.class, args);
    }
}