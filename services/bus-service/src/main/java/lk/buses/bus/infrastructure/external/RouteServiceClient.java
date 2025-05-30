package lk.buses.bus.infrastructure.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "route-service", fallback = RouteServiceFallback.class)
public interface RouteServiceClient {

    @GetMapping("/api/v1/routes/{routeId}")
    RouteInfo getRoute(@PathVariable UUID routeId);

    @lombok.Data
    class RouteInfo {
        private UUID id;
        private String routeNumber;
        private String routeNameEn;
        private String originCity;
        private String destinationCity;
    }
}

// Fallback implementation
@Component
@Slf4j
class RouteServiceFallback implements RouteServiceClient {

    @Override
    public RouteInfo getRoute(UUID routeId) {
        log.warn("Route service unavailable, returning fallback for route: {}", routeId);
        RouteInfo fallback = new RouteInfo();
        fallback.setId(routeId);
        fallback.setRouteNumber("Unknown");
        fallback.setRouteNameEn("Route information unavailable");
        return fallback;
    }
}