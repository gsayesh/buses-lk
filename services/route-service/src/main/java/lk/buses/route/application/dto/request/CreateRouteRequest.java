package lk.buses.route.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateRouteRequest {
    @NotBlank(message = "Route number is required")
    @Size(max = 20)
    private String routeNumber;

    @NotBlank(message = "Route name in English is required")
    @Size(max = 200)
    private String routeNameEn;

    @NotBlank(message = "Route name in Sinhala is required")
    @Size(max = 200)
    private String routeNameSi;

    @NotBlank(message = "Route name in Tamil is required")
    @Size(max = 200)
    private String routeNameTa;

    @NotBlank(message = "Origin city is required")
    @Size(max = 100)
    private String originCity;

    @NotBlank(message = "Destination city is required")
    @Size(max = 100)
    private String destinationCity;

    @Positive
    private Double totalDistanceKm;

    private boolean isRotational;
    private String routePhotoUrl;

    @NotEmpty(message = "Route must have at least 2 stops")
    @Size(min = 2)
    private List<CreateRouteStopRequest> stops;
}