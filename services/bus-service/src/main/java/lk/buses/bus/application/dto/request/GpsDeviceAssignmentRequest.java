package lk.buses.bus.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class GpsDeviceAssignmentRequest {
    @NotNull(message = "Bus ID is required")
    private UUID busId;

    @NotBlank(message = "GPS device ID is required")
    @Size(max = 100)
    private String gpsDeviceId;
}
