package lk.buses.user.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;


@Data
public class DriverRegistrationRequest {
    @NotBlank(message = "License number is required")
    @Size(max = 50)
    private String licenseNumber;

    @NotNull(message = "Operator ID is required")
    private UUID operatorId;
}