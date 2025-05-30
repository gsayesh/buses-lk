package lk.buses.bus.infrastructure.cache;

import lk.buses.bus.application.dto.request.LocationUpdateRequest;
import lk.buses.bus.application.dto.response.BusLocationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class BusLocationCache {

    private static final String LOCATION_KEY_PREFIX = "bus:location:";
    private static final String GEO_KEY = "buses:geo";
    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    private final RedisTemplate<String, Object> redisTemplate;

    public void updateLocation(UUID busId, LocationUpdateRequest location) {
        try {
            String key = LOCATION_KEY_PREFIX + busId;

            BusLocationResponse locationData = BusLocationResponse.builder()
                    .busId(busId)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .speedKmh(location.getSpeed())
                    .heading(location.getHeading())
                    .trackingSource(location.getTrackingSource())
                    .lastUpdated(Instant.now())
                    .isMoving(location.getSpeed() != null && location.getSpeed() > 5.0)
                    .build();

            // Store location data
            redisTemplate.opsForValue().set(key, locationData, CACHE_TTL);

            // Update geo index
            GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
            geoOps.add(GEO_KEY, new Point(location.getLongitude(), location.getLatitude()), busId.toString());

            log.debug("Updated location cache for bus: {}", busId);
        } catch (Exception e) {
            log.error("Failed to update location cache for bus: {}", busId, e);
        }
    }

    public BusLocationResponse getLocation(UUID busId) {
        try {
            String key = LOCATION_KEY_PREFIX + busId;
            return (BusLocationResponse) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Failed to get location from cache for bus: {}", busId, e);
            return null;
        }
    }
}