package lk.buses.bus.application.dto.response;

import lk.buses.common.core.enums.BusOperatorType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class BusOperatorResponse {
    private UUID id;
    private String operatorCode;
    private String operatorName;
    private BusOperatorType operatorType;
    private String contactNumber;
    private String email;
    private String address;
    private boolean isActive;
    private Integer busCount;
    private Instant createdAt;
}
