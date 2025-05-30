package lk.buses.bus.application.dto.request;

import jakarta.validation.constraints.*;
import lk.buses.common.core.enums.BusOperatorType;
import lk.buses.common.core.enums.ServiceCategory;
import lk.buses.common.core.enums.TrackingSource;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

// Create Bus Operator Request
@Data
public class CreateOperatorRequest {
    @NotBlank(message = "Operator code is required")
    @Size(max = 20)
    private String operatorCode;

    @NotBlank(message = "Operator name is required")
    @Size(max = 100)
    private String operatorName;

    @NotNull(message = "Operator type is required")
    private BusOperatorType operatorType;

    @Pattern(regexp = "^(\\+94|0)?[0-9]{9}$", message = "Invalid Sri Lankan phone number")
    private String contactNumber;

    @Email(message = "Invalid email format")
    @Size(max = 100)
    private String email;

    private String address;
}

// Register Bus Request
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

    @Min(1990)
    @Max(2025)
    private Integer yearOfManufacture;

    @NotNull(message = "Seating capacity is required")
    @Min(10)
    @Max(100)
    private Integer seatingCapacity;

    private String busPhotoUrl;
}

// Create Schedule Request
@Data
public class CreateScheduleRequest {
    @NotNull(message = "Route ID is required")
    private UUID routeId;

    private UUID busId; // Optional for rotational routes

    @NotNull(message = "Departure time is required")
    private LocalTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalTime arrivalTime;

    @NotEmpty(message = "Days of week are required")
    @Size(min = 1, max = 7)
    private List<Integer> daysOfWeek;
}

// Location Update Request
@Data
public class LocationUpdateRequest {
    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double longitude;

    @PositiveOrZero
    private Double speed;

    @Min(0)
    @Max(359)
    private Double heading;

    private UUID driverId;

    @NotNull(message = "Tracking source is required")
    private TrackingSource trackingSource;
}

// Bus Search Request
@Data
public class BusSearchRequest {
    private UUID operatorId;
    private ServiceCategory serviceCategory;
    private Integer minCapacity;
    private Boolean hasGps;
    private Boolean isActive = true;
}

// GPS Device Assignment Request
@Data
public class GpsDeviceAssignmentRequest {
    @NotNull(message = "Bus ID is required")
    private UUID busId;

    @NotBlank(message = "GPS device ID is required")
    @Size(max = 100)
    private String gpsDeviceId;
}