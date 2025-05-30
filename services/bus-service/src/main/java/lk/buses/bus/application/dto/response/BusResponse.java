package lk.buses.bus.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class BusResponse {
    private UUID id;
    private String registrationNumber;
    private BusOperatorResponse operator;
    private lk.buses.bus.application.dto.response.ServiceCategoryResponse serviceCategory;
    private String make;
    private String model;
    private Integer yearOfManufacture;
    private Integer seatingCapacity;
    private boolean hasGpsDevice;
    private String gpsDeviceId;
    private String busPhotoUrl;
    private boolean isActive;
    private Instant createdAt;
}
