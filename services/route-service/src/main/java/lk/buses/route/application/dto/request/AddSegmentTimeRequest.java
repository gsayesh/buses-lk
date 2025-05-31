package lk.buses.route.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

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