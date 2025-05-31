// RouteOption.java
package lk.buses.route.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RouteOption {
    private UUID routeId;
    private String routeNumber;
    private String routeName;
    private String fromStop;
    private String toStop;
    private Double distanceKm;
    private String estimatedDuration;
    private FareBreakdownResponse fares;
}