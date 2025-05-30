package lk.buses.bus.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class TrackingPoint {
    private Double latitude;
    private Double longitude;
    private Double speedKmh;
    private Double heading;
    private Instant timestamp;
}
