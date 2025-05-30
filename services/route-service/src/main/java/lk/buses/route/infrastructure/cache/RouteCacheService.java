package lk.buses.route.infrastructure.cache;

import lk.buses.route.application.dto.response.RouteResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RouteCacheService {

    @Cacheable(value = "routes", key = "#routeId", unless = "#result == null")
    public RouteResponse getRoute(UUID routeId) {
        log.debug("Cache miss for route: {}", routeId);
        return null; // Will be populated by the caller
    }

    @CachePut(value = "routes", key = "#routeId")
    public RouteResponse cacheRoute(UUID routeId, RouteResponse route) {
        log.debug("Caching route: {}", routeId);
        return route;
    }

    @CacheEvict(value = "routes", key = "#routeId")
    public void evictRoute(UUID routeId) {
        log.debug("Evicting route from cache: {}", routeId);
    }

    @CacheEvict(value = "routes", allEntries = true)
    public void evictAllRoutes() {
        log.debug("Evicting all routes from cache");
    }
}