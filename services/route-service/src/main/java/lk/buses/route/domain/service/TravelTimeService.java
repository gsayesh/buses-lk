package lk.buses.route.domain.service;

import lk.buses.route.domain.entity.RouteSegmentTime;
import lk.buses.route.domain.repository.RouteSegmentTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TravelTimeService {

    private final RouteSegmentTimeRepository segmentTimeRepository;

    @Cacheable(value = "travelTimes", key = "#routeId + ':' + #fromStopId + ':' + #toStopId + ':' + #departureTime")
    public TravelTimeEstimate estimateTravelTime(UUID routeId, UUID fromStopId, UUID toStopId,
                                                 LocalTime departureTime) {
        log.debug("Estimating travel time for route: {} at {}", routeId, departureTime);

        String timePeriod = determineTimePeriod(departureTime);

        List<RouteSegmentTime> segments = segmentTimeRepository
                .findSegmentsBetweenStops(routeId, fromStopId, toStopId, timePeriod);

        int totalMinutes = segments.stream()
                .mapToInt(RouteSegmentTime::getDurationMinutes)
                .sum();

        double totalDistance = segments.stream()
                .mapToDouble(segment -> segment.getDistanceKm().doubleValue())
                .sum();

        LocalTime estimatedArrival = departureTime.plusMinutes(totalMinutes);

        return TravelTimeEstimate.builder()
                .departureTime(departureTime)
                .estimatedArrival(estimatedArrival)
                .duration(Duration.ofMinutes(totalMinutes))
                .distanceKm(totalDistance)
                .timePeriod(timePeriod)
                .segmentCount(segments.size())
                .build();
    }

    private String determineTimePeriod(LocalTime time) {
        int hour = time.getHour();

        // Peak hours: 6-9 AM and 5-8 PM
        if ((hour >= 6 && hour < 9) || (hour >= 17 && hour < 20)) {
            return "peak";
        }
        // Night hours: 9 PM - 5 AM
        else if (hour >= 21 || hour < 5) {
            return "night";
        }
        // Off-peak hours
        else if (hour >= 10 && hour < 16) {
            return "off_peak";
        }
        // Regular day hours
        else {
            return "day";
        }
    }

    @lombok.Data
    @lombok.Builder
    public static class TravelTimeEstimate {
        private LocalTime departureTime;
        private LocalTime estimatedArrival;
        private Duration duration;
        private double distanceKm;
        private String timePeriod;
        private int segmentCount;
    }
}