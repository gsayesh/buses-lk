package lk.buses.bus.application.dto.request;

import jakarta.validation.constraints.*;
import lk.buses.common.core.enums.ServiceCategory;
import lombok.Data;

import java.util.UUID;

@Data
public class RegisterBusRequest {
    @NotBlank(message = "Registration number is required")
    @Pattern(regexp = "^[A-Z]{2,3}-\\d{4}$", message = "Invalid registration number format")
    private String registrationNumber;

    @NotNull(message = "Operator ID is required")
    private UUID operatorId;

    @NotNull(message = "Service category is required")
    private ServiceCategory serviceCategory;

    @Size(max = 50)
    private String make;

    @Size(max = 50)
    private String model;

    @Min(1990) @Max(2025)
    private Integer yearOfManufacture;

    @NotNull(message = "Seating capacity is required")
    @Min(10) @Max(100)
    private Integer seatingCapacity;

    private String busPhotoUrl;
}
