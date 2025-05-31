package lk.buses.route.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GovernmentFareUpdateRequest {
    @NotNull(message = "New minimum fare is required")
    @DecimalMin("0.0")
    private BigDecimal newMinimumFare;

    @NotNull(message = "Percentage increase is required")
    @DecimalMin("0.0") @DecimalMax("100.0")
    private BigDecimal percentageIncrease;

    @NotNull(message = "Effective date is required")
    @Future
    private LocalDate effectiveDate;

    private String notes;
}