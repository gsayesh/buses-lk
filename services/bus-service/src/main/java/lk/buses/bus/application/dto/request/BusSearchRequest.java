package lk.buses.bus.application.dto.request;

import lk.buses.common.core.enums.ServiceCategory;
import lombok.Data;

import java.util.UUID;

@Data
public class BusSearchRequest {
    private UUID operatorId;
    private ServiceCategory serviceCategory;
    private Integer minCapacity;
    private Boolean hasGps;
    private Boolean isActive = true;
}
