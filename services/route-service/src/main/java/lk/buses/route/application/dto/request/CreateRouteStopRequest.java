package lk.buses.route.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateRouteStopRequest {
    @NotNull(message = "Stop sequence is required")
    @Positive
    private Integer stopSequence;

    @NotBlank(message = "Stop name in English is required")
    @Size(max = 100)
    private String stopNameEn;

    @NotBlank(message = "Stop name in Sinhala is required")
    @Size(max = 100)
    private String stopNameSi;

    @NotBlank(message = "Stop name in Tamil is required")
    @Size(max = 100)
    private String stopNameTa;

    @DecimalMin("-90.0") @DecimalMax("90.0")
    private Double latitude;

    @DecimalMin("-180.0") @DecimalMax("180.0")
    private Double longitude;

    private boolean isMajorStop;
}