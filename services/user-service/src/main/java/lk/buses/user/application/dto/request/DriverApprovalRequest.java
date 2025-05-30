package lk.buses.user.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class DriverApprovalRequest {
    @NotNull(message = "Driver profile ID is required")
    private UUID driverProfileId;

    private String remarks;
}