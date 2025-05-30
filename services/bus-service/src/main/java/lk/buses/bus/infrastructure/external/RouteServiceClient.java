package lk.buses.bus.infrastructure.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "route-service", fallback = RouteServiceFallback.class)
public interface RouteServiceClient {

    @GetMapping("/api/v1/routes/{routeId}")
    RouteInfo getRoute(@PathVariable UUID routeId);

    /**
     * Route information DTO for inter-service communication
     */
    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Builder
    class RouteInfo {
        private UUID id;
        private String routeNumber;
        private String routeNameEn;
        private String originCity;
        private String destinationCity;
        private Double totalDistanceKm;
        private boolean isRotational;
    }
}