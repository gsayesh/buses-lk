package lk.buses.bus.application.dto.response;

import lk.buses.common.core.enums.TrackingSource;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class BusLocationResponse {
    private UUID busId;
    private String registrationNumber;
    private Double latitude;
    private Double longitude;
    private Double speedKmh;
    private Double heading;
    private String driverName;
    private TrackingSource trackingSource;
    private Instant lastUpdated;
    private boolean isMoving;
}
