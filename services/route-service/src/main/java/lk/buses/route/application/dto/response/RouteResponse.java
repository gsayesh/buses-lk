package lk.buses.route.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class RouteResponse {
    private UUID id;
    private String routeNumber;
    private String routeNameEn;
    private String routeNameSi;
    private String routeNameTa;
    private String originCity;
    private String destinationCity;
    private Double totalDistanceKm;
    private boolean isRotational;
    private String routePhotoUrl;
    private boolean isActive;
    private List<RouteStopResponse> stops;
    private Instant createdAt;
}