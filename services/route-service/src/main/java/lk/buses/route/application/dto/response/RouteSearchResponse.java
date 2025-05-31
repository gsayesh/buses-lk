package lk.buses.route.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RouteSearchResponse {
    private List<RouteOption> routes;
    private int totalResults;
}