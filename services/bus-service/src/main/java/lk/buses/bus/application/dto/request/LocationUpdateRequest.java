package lk.buses.bus.application.dto.request;

import jakarta.validation.constraints.*;
import lk.buses.common.core.enums.TrackingSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationUpdateRequest {
    @NotNull(message = "Latitude is required")
    @DecimalMin("-90.0") @DecimalMax("90.0")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin("-180.0") @DecimalMax("180.0")
    private Double longitude;

    @PositiveOrZero
    private Double speed;

    @Min(0) @Max(359)
    private Double heading;

    private UUID driverId;

    @NotNull(message = "Tracking source is required")
    private TrackingSource trackingSource;
}