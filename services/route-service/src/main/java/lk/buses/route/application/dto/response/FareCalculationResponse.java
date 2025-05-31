package lk.buses.route.application.dto.response;

import lk.buses.common.core.enums.ServiceCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class FareCalculationResponse {
    private UUID routeId;
    private String routeNumber;
    private String fromStop;
    private String toStop;
    private ServiceCategory serviceCategory;
    private BigDecimal baseFare;
    private BigDecimal finalFare;
    private BigDecimal minimumFare;
    private String currency = "LKR";
}