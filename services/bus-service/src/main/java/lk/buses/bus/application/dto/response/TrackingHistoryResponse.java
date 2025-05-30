package lk.buses.bus.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class TrackingHistoryResponse {
    private UUID busId;
    private String registrationNumber;
    private List<TrackingPoint> trackingPoints;
    private Double totalDistance;
    private Double averageSpeed;
    private Double maxSpeed;
}
