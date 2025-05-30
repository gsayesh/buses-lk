package lk.buses.route.application.dto.response;

import lk.buses.common.core.enums.ServiceCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

// Route Response
@Data
@Builder
public class RouteResponse {
    private UUID id;
    private String routeNumber;
    private String routeNameEn;
    private String routeNameSi;
    private String routeNameTa;
    private String originCity;
    private String destinationCity;
    private Double totalDistanceKm;
    private boolean isRotational;
    private String routePhotoUrl;
    private boolean isActive;
    private List<RouteStopResponse> stops;
    private Instant createdAt;
}

// Route Stop Response
@Data
@Builder
public class RouteStopResponse {
    private UUID id;
    private Integer stopSequence;
    private String stopNameEn;
    private String stopNameSi;
    private String stopNameTa;
    private Double latitude;
    private Double longitude;
    private boolean isMajorStop;
}

// Fare Calculation Response
@Data
@Builder
public class FareCalculationResponse {
    private UUID routeId;
    private String routeNumber;
    private String fromStop;
    private String toStop;
    private ServiceCategory serviceCategory;
    private BigDecimal baseFare;
    private BigDecimal finalFare;
    private BigDecimal minimumFare;
    private String currency = "LKR";
}

// Fare Breakdown Response
@Data
@Builder
public class FareBreakdownResponse {
    private UUID routeId;
    private String routeNumber;
    private String fromStop;
    private String toStop;
    private BigDecimal normalFare;
    private BigDecimal semiLuxuryFare;
    private BigDecimal acLuxuryFare;
    private BigDecimal superLuxuryFare;
    private String currency = "LKR";
}

// Travel Time Response
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

// Route Search Response
@Data
@Builder
public class RouteSearchResponse {
    private List<RouteOption> routes;
    private int totalResults;
}

@Data
@Builder
public class RouteOption {
    private UUID routeId;
    private String routeNumber;
    private String routeName;
    private String fromStop;
    private String toStop;
    private Double distanceKm;
    private String estimatedDuration;
    private FareBreakdownResponse fares;
}

// Fare Structure Response
@Data
@Builder
public class FareStructureResponse {
    private UUID id;
    private LocalDate effectiveDate;
    private BigDecimal minimumFare;
    private BigDecimal farePerKm;
    private boolean isActive;
    private String notes;
    private Instant createdAt;
}