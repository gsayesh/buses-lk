package lk.buses.route.domain.service;

import lk.buses.common.core.exception.BusinessException;
import lk.buses.common.core.exception.ResourceNotFoundException;
import lk.buses.route.domain.entity.Route;
import lk.buses.route.domain.entity.RouteStop;
import lk.buses.route.domain.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RouteDomainService {

    private final RouteRepository routeRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Transactional
    public Route createRoute(Route route) {
        log.debug("Creating route: {}", route.getRouteNumber());

        // Validate unique route number
        if (routeRepository.findByRouteNumber(route.getRouteNumber()).isPresent()) {
            throw new BusinessException("Route number already exists", "ROUTE_EXISTS");
        }

        // Set up bidirectional relationships
        for (RouteStop stop : route.getStops()) {
            stop.setRoute(route);
            if (stop.getLatitude() != null && stop.getLongitude() != null) {
                stop.setLocation(geometryFactory.createPoint(
                        new Coordinate(stop.getLongitude(), stop.getLatitude())
                ));
            }
        }

        return routeRepository.save(route);
    }

    @Transactional
    public Route updateRoute(UUID routeId, Route updates) {
        log.debug("Updating route: {}", routeId);

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route", routeId.toString()));

        // Update basic fields
        if (updates.getRouteNameEn() != null) {
            route.setRouteNameEn(updates.getRouteNameEn());
        }
        if (updates.getRouteNameSi() != null) {
            route.setRouteNameSi(updates.getRouteNameSi());
        }
        if (updates.getRouteNameTa() != null) {
            route.setRouteNameTa(updates.getRouteNameTa());
        }
        if (updates.getTotalDistanceKm() != null) {
            route.setTotalDistanceKm(updates.getTotalDistanceKm());
        }
        if (updates.getRoutePhotoUrl() != null) {
            route.setRoutePhotoUrl(updates.getRoutePhotoUrl());
        }

        return routeRepository.save(route);
    }

    @Transactional
    public void addStop(UUID routeId, RouteStop stop) {
        log.debug("Adding stop to route: {}", routeId);

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route", routeId.toString()));

        // Set location
        if (stop.getLatitude() != null && stop.getLongitude() != null) {
            stop.setLocation(geometryFactory.createPoint(
                    new Coordinate(stop.getLongitude(), stop.getLatitude())
            ));
        }

        stop.setRoute(route);
        route.getStops().add(stop);

        routeRepository.save(route);
    }

    @Transactional
    public void deactivateRoute(UUID routeId) {
        log.debug("Deactivating route: {}", routeId);

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route", routeId.toString()));

        route.setActive(false);
        routeRepository.save(route);
    }

    public List<Route> findRoutesBetweenCities(String fromCity, String toCity) {
        log.debug("Finding routes between {} and {}", fromCity, toCity);

        return routeRepository.findRoutesBetweenStops(fromCity, toCity);
    }

    public List<Route> findActiveRoutes() {
        return routeRepository.findByIsActiveTrue();
    }
}