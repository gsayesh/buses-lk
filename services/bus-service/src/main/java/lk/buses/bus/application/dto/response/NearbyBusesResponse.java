package lk.buses.bus.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NearbyBusesResponse {
    private List<BusLocationWithDistance> buses;
    private Double searchRadiusKm;
    private Integer totalFound;
}
