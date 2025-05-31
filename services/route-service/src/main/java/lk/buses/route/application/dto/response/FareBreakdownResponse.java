package lk.buses.route.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class FareBreakdownResponse {
    private UUID routeId;
    private String routeNumber;
    private String fromStop;
    private String toStop;
    private BigDecimal normalFare;
    private BigDecimal semiLuxuryFare;
    private BigDecimal acLuxuryFare;
    private BigDecimal superLuxuryFare;
    private String currency = "LKR";
}