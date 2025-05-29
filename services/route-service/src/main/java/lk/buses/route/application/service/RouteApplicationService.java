package lk.buses.route.application.service;

import lk.buses.common.core.dto.PageableRequest;
import lk.buses.common.core.exception.ResourceNotFoundException;
import lk.buses.route.application.dto.request.*;
import lk.buses.route.application.dto.response.*;
import lk.buses.route.application.mapper.RouteMapper;
import lk.buses.route.domain.entity.Route;
import lk.buses.route.domain.entity.RouteStop;
import lk.buses.route.domain.repository.RouteRepository;
import lk.buses.route.domain.repository.RouteStopRepository;
import lk.buses.route.domain.service.RouteDomainService;
import lk.buses.route.infrastructure.cache.RouteCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RouteApplicationService {

    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final RouteDomainService routeDomainService;
    private final RouteMapper routeMapper;
    private final RouteCacheService cacheService;

    @Transactional
    public RouteResponse createRoute(CreateRouteRequest request) {
        log.info("Creating new route: {}", request.getRouteNumber());

        Route route = routeMapper.toEntity(request);

        // Map stops
        List<RouteStop> stops = request.getStops().stream()
                .map(routeMapper::toEntity)
                .collect(Collectors.toList());
        route.setStops(stops);

        route = routeDomainService.createRoute(route);

        log.info("Route created successfully: {}", route.getId());
        return routeMapper.toResponse(route);
    }

    public RouteResponse getRouteById(UUID routeId) {
        log.debug("Getting route by ID: {}", routeId);

        // Try cache first
        RouteResponse cached = cacheService.getRoute(routeId);
        if (cached != null) {
            return cached;
        }

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route", routeId.toString()));

        RouteResponse response = routeMapper.toResponse(route);
        cacheService.cacheRoute(routeId, response);

        return response;
    }

    public RouteResponse getRouteByNumber(String routeNumber) {
        log.debug("Getting route by number: {}", routeNumber);

        Route route = routeRepository.findByRouteNumber(routeNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with number: " + routeNumber));

        return routeMapper.toResponse(route);
    }

    public List<RouteResponse> searchRoutes(RouteSearchRequest request) {
        log.debug("Searching routes from {} to {}", request.getFrom(), request.getTo());

        List<Route> routes = routeDomainService.findRoutesBetweenCities(
                request.getFrom(),
                request.getTo()
        );

        return routeMapper.toResponseList(routes);
    }

    public Page<RouteResponse> getAllRoutes(PageableRequest request) {
        log.debug("Getting all routes - page: {}, size: {}", request.getPage(), request.getSize());

        PageRequest pageRequest = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Sort.Direction.valueOf(request.getSortDirection()), request.getSortBy())
        );

        Page<Route> routes = routeRepository.findAll(pageRequest);
        return routes.map(routeMapper::toResponse);
    }

    @Transactional
    public void addStopToRoute(UUID routeId, CreateRouteStopRequest request) {
        log.info("Adding stop to route: {}", routeId);

        RouteStop stop = routeMapper.toEntity(request);
        routeDomainService.addStop(routeId, stop);

        // Invalidate cache
        cacheService.evictRoute(routeId);
    }

    public List<RouteStopResponse> getRouteStops(UUID routeId) {
        log.debug("Getting stops for route: {}", routeId);

        List<RouteStop> stops = routeStopRepository.findByRouteIdOrderByStopSequence(routeId);
        return stops.stream()
                .map(routeMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<RouteStopResponse> getNearbyStops(double latitude, double longitude, double radiusKm) {
        log.debug("Finding stops near ({}, {}) within {}km", latitude, longitude, radiusKm);

        List<RouteStop> stops = routeStopRepository.findNearbyStops(
                latitude,
                longitude,
                radiusKm * 1000 // Convert to meters
        );

        return stops.stream()
                .map(routeMapper::toResponse)
                .collect(Collectors.toList());
    }
}
