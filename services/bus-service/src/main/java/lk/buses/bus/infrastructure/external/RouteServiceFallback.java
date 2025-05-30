package lk.buses.bus.infrastructure.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RouteServiceFallback implements RouteServiceClient {

    @Override
    public RouteInfo getRoute(UUID routeId) {
        log.warn("Route service unavailable, returning fallback for route: {}", routeId);

        RouteInfo fallback = new RouteInfo();
        fallback.setId(routeId);
        fallback.setRouteNumber("Unknown");
        fallback.setRouteNameEn("Route information unavailable");
        fallback.setOriginCity("Unknown");
        fallback.setDestinationCity("Unknown");

        return fallback;
    }
}