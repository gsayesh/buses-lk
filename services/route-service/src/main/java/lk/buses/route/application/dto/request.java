package lk.buses.route.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

// Create Route Request
@Data
public class CreateRouteRequest {
    @NotBlank(message = "Route number is required")
    @Size(max = 20)
    private String routeNumber;

    @NotBlank(message = "Route name in English is required")
    @Size(max = 200)
    private String routeNameEn;

    @NotBlank(message = "Route name in Sinhala is required")
    @Size(max = 200)
    private String routeNameSi;

    @NotBlank(message = "Route name in Tamil is required")
    @Size(max = 200)
    private String routeNameTa;

    @NotBlank(message = "Origin city is required")
    @Size(max = 100)
    private String originCity;

    @NotBlank(message = "Destination city is required")
    @Size(max = 100)
    private String destinationCity;

    @Positive
    private Double totalDistanceKm;

    private boolean isRotational;

    private String routePhotoUrl;

    @NotEmpty(message = "Route must have at least 2 stops")
    @Size(min = 2)
    private List<CreateRouteStopRequest> stops;
}

// Create Route Stop Request
@Data
public class CreateRouteStopRequest {
    @NotNull(message = "Stop sequence is required")
    @Positive
    private Integer stopSequence;

    @NotBlank(message = "Stop name in English is required")
    @Size(max = 100)
    private String stopNameEn;

    @NotBlank(message = "Stop name in Sinhala is required")
    @Size(max = 100)
    private String stopNameSi;

    @NotBlank(message = "Stop name in Tamil is required")
    @Size(max = 100)
    private String stopNameTa;

    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double latitude;

    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double longitude;

    private boolean isMajorStop;
}

// Add Segment Time Request
@Data
public class AddSegmentTimeRequest {
    @NotNull(message = "From stop ID is required")
    private UUID fromStopId;

    @NotNull(message = "To stop ID is required")
    private UUID toStopId;

    @NotBlank(message = "Time period is required")
    @Pattern(regexp = "^(day|night|peak|off_peak)$", message = "Invalid time period")
    private String timePeriod;

    @NotNull(message = "Duration is required")
    @Positive
    private Integer durationMinutes;

    @Positive
    private BigDecimal distanceKm;
}

// Fare Calculation Request
@Data
public class FareCalculationRequest {
    @NotNull(message = "Route ID is required")
    private UUID routeId;

    @NotNull(message = "From stop ID is required")
    private UUID fromStopId;

    @NotNull(message = "To stop ID is required")
    private UUID toStopId;

    @NotNull(message = "Service category is required")
    private ServiceCategory serviceCategory;
}

// Route Search Request
@Data
public class RouteSearchRequest {
    @NotBlank(message = "From location is required")
    private String from;

    @NotBlank(message = "To location is required")
    private String to;
}

// Government Fare Update Request
@Data
public class GovernmentFareUpdateRequest {
    @NotNull(message = "New minimum fare is required")
    @DecimalMin(value = "0.0")
    private BigDecimal newMinimumFare;

    @NotNull(message = "Percentage increase is required")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private BigDecimal percentageIncrease;

    @NotNull(message = "Effective date is required")
    @Future
    private LocalDate effectiveDate;

    private String notes;
}
