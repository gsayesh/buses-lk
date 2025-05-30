package lk.buses.bus.domain.service;

import lk.buses.bus.domain.entity.Bus;
import lk.buses.bus.domain.entity.BusTracking;
import lk.buses.bus.domain.repository.BusRepository;
import lk.buses.bus.domain.repository.BusTrackingRepository;
import lk.buses.common.core.enums.TrackingSource;
import lk.buses.common.core.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrackingDomainService {

    private static final double SPEED_LIMIT_HIGHWAY = 80.0;
    private static final double SPEED_LIMIT_CITY = 50.0;
    private static final Duration TRACKING_STALENESS = Duration.ofMinutes(5);

    private final BusTrackingRepository trackingRepository;
    private final BusRepository busRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Transactional
    public BusTracking recordLocation(UUID busId, UUID driverId, double latitude, double longitude,
                                      Double speed, Double heading, TrackingSource source) {
        log.debug("Recording location for bus: {} at ({}, {})", busId, latitude, longitude);

        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus", busId.toString()));

        BusTracking tracking = BusTracking.builder()
                .bus(bus)
                .driverId(driverId)
                .latitude(latitude)
                .longitude(longitude)
                .location(geometryFactory.createPoint(new Coordinate(longitude, latitude)))
                .speedKmh(speed)
                .heading(heading)
                .trackingSource(source)
                .timestamp(Instant.now())
                .build();

        return trackingRepository.save(tracking);
    }

    public Optional<BusTracking> getLatestLocation(UUID busId) {
        return trackingRepository.findLatestTracking(busId)
                .filter(tracking -> !isStale(tracking));
    }

    public List<BusTracking> getBusesNearLocation(double latitude, double longitude, double radiusKm) {
        log.debug("Finding buses within {}km of ({}, {})", radiusKm, latitude, longitude);

        Instant since = Instant.now().minus(TRACKING_STALENESS);
        return trackingRepository.findBusesWithinRadius(
                latitude, longitude, radiusKm * 1000, since
        );
    }

    public List<BusTracking> getRecentSpeedViolations() {
        Instant since = Instant.now().minus(Duration.ofHours(1));
        return trackingRepository.findSpeedViolations(SPEED_LIMIT_HIGHWAY, since);
    }

    public List<BusTracking> getBusTrackingHistory(UUID busId, Duration period) {
        Instant since = Instant.now().minus(period);
        return trackingRepository.findRecentTracking(busId, since);
    }

    private boolean isStale(BusTracking tracking) {
        return Duration.between(tracking.getTimestamp(), Instant.now())
                .compareTo(TRACKING_STALENESS) > 0;
    }
}