package lk.buses.route.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RouteSearchRequest {
    @NotBlank(message = "From location is required")
    private String from;

    @NotBlank(message = "To location is required")
    private String to;
}