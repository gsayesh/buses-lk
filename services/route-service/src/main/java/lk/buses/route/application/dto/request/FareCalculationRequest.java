package lk.buses.route.application.dto.request;

import jakarta.validation.constraints.*;
import lk.buses.common.core.enums.ServiceCategory;
import lombok.Data;

import java.util.UUID;

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