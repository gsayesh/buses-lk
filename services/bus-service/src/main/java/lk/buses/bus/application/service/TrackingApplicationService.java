package lk.buses.bus.application.service;

import lk.buses.bus.application.dto.request.LocationUpdateRequest;
import lk.buses.bus.application.dto.response.*;
import lk.buses.bus.domain.entity.BusTracking;
import lk.buses.bus.domain.service.TrackingDomainService;
import lk.buses.bus.infrastructure.cache.BusLocationCache;
import lk.buses.common.messaging.event.BusLocationUpdatedEvent;
import lk.buses.common.messaging.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static lk.buses.common.messaging.config.RabbitMQConfig.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrackingApplicationService {

    private final TrackingDomainService trackingDomainService;
    private final BusLocationCache locationCache;
    private final EventPublisher eventPublisher;

    @Transactional
    public void updateBusLocation(UUID busId, LocationUpdateRequest request) {
        log.debug("Updating location for bus: {}", busId);

        // Record in database
        BusTracking tracking = trackingDomainService.recordLocation(
                busId,
                request.getDriverId(),
                request.getLatitude(),
                request.getLongitude(),
                request.getSpeed(),
                request.getHeading(),
                request.getTrackingSource()
        );

        // Update cache
        locationCache.updateLocation(busId, request);

        // Publish event
        BusLocationUpdatedEvent event = BusLocationUpdatedEvent.builder()
                .busId(busId)
                .driverId(request.getDriverId())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .speed(request.getSpeed())
                .heading(request.getHeading())
                .trackingSource(request.getTrackingSource())
                .build();

        eventPublisher.publish(BUS_EXCHANGE, BUS_LOCATION_KEY, event);
    }

    public BusLocationResponse getCurrentLocation(UUID busId) {
        log.debug("Getting current location for bus: {}", busId);

        // Try cache first
        BusLocationResponse cached = locationCache.getLocation(busId);
        if (cached != null) {
            return cached;
        }

        // Fallback to database
        return trackingDomainService.getLatestLocation(busId)
                .map(this::mapToLocationResponse)
                .orElse(null);
    }

    public NearbyBusesResponse findNearbyBuses(double latitude, double longitude, double radiusKm) {
        log.debug("Finding buses within {}km of ({}, {})", radiusKm, latitude, longitude);

        List<BusTracking> nearbyBuses = trackingDomainService.getBusesNearLocation(
                latitude, longitude, radiusKm
        );

        List<BusLocationWithDistance> busesWithDistance = nearbyBuses.stream()
                .map(tracking -> {
                    double distance = calculateDistance(latitude, longitude,
                            tracking.getLatitude(), tracking.getLongitude());

                    return BusLocationWithDistance.builder()
                            .busLocation(mapToLocationResponse(tracking))
                            .distanceKm(distance)
                            .direction(calculateDirection(latitude, longitude,
                                    tracking.getLatitude(), tracking.getLongitude()))
                            .build();
                })
                .sorted((a, b) -> Double.compare(a.getDistanceKm(), b.getDistanceKm()))
                .collect(Collectors.toList());

        return NearbyBusesResponse.builder()
                .buses(busesWithDistance)
                .searchRadiusKm(radiusKm)
                .totalFound(busesWithDistance.size())
                .build();
    }

    public TrackingHistoryResponse getBusTrackingHistory(UUID busId, Duration period) {
        log.debug("Getting tracking history for bus: {} for period: {}", busId, period);

        List<BusTracking> history = trackingDomainService.getBusTrackingHistory(busId, period);

        List<TrackingPoint> trackingPoints = history.stream()
                .map(tracking -> TrackingPoint.builder()
                        .latitude(tracking.getLatitude())
                        .longitude(tracking.getLongitude())
                        .speedKmh(tracking.getSpeedKmh())
                        .heading(tracking.getHeading())
                        .timestamp(tracking.getTimestamp())
                        .build())
                .collect(Collectors.toList());

        // Calculate statistics
        double totalDistance = calculateTotalDistance(trackingPoints);
        double averageSpeed = history.stream()
                .filter(t -> t.getSpeedKmh() != null)
                .mapToDouble(BusTracking::getSpeedKmh)
                .average()
                .orElse(0.0);
        double maxSpeed = history.stream()
                .filter(t -> t.getSpeedKmh() != null)
                .mapToDouble(BusTracking::getSpeedKmh)
                .max()
                .orElse(0.0);

        return TrackingHistoryResponse.builder()
                .busId(busId)
                .trackingPoints(trackingPoints)
                .totalDistance(totalDistance)
                .averageSpeed(averageSpeed)
                .maxSpeed(maxSpeed)
                .build();
    }

    private BusLocationResponse mapToLocationResponse(BusTracking tracking) {
        return BusLocationResponse.builder()
                .busId(tracking.getBus().getId())
                .registrationNumber(tracking.getBus().getRegistrationNumber())
                .latitude(tracking.getLatitude())
                .longitude(tracking.getLongitude())
                .speedKmh(tracking.getSpeedKmh())
                .heading(tracking.getHeading())
                .trackingSource(tracking.getTrackingSource())
                .lastUpdated(tracking.getTimestamp())
                .isMoving(tracking.getSpeedKmh() != null && tracking.getSpeedKmh() > 5.0)
                .build();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula
        double R = 6371; // Earth's radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    private String calculateDirection(double fromLat, double fromLon, double toLat, double toLon) {
        double angle = Math.toDegrees(Math.atan2(toLon - fromLon, toLat - fromLat));
        if (angle < 0) angle += 360;

        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        int index = (int) Math.round(angle / 45) % 8;
        return directions[index];
    }

    private double calculateTotalDistance(List<TrackingPoint> points) {
        double total = 0.0;
        for (int i = 1; i < points.size(); i++) {
            TrackingPoint prev = points.get(i - 1);
            TrackingPoint curr = points.get(i);
            total += calculateDistance(prev.getLatitude(), prev.getLongitude(),
                    curr.getLatitude(), curr.getLongitude());
        }
        return total;
    }
}