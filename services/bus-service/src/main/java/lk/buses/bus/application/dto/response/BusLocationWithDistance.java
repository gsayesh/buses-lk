package lk.buses.bus.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusLocationWithDistance {
    private BusLocationResponse busLocation;
    private Double distanceKm;
    private String direction;
}
