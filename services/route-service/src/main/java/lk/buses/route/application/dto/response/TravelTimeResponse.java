package lk.buses.route.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class TravelTimeResponse {
    private UUID routeId;
    private String routeNumber;
    private String fromStop;
    private String toStop;
    private LocalTime departureTime;
    private LocalTime estimatedArrival;
    private String duration;
    private Double distanceKm;
    private String timePeriod;
}