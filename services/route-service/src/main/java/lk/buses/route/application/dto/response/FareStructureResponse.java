package lk.buses.route.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class FareStructureResponse {
    private UUID id;
    private LocalDate effectiveDate;
    private BigDecimal minimumFare;
    private BigDecimal farePerKm;
    private boolean isActive;
    private String notes;
    private Instant createdAt;
}