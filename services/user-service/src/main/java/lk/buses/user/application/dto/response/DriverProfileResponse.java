package lk.buses.user.application.dto.response;

import lk.buses.common.core.enums.UserRole;
import lk.buses.common.core.enums.LanguageCode;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class DriverProfileResponse {
    private UUID id;
    private UUID userId;
    private String licenseNumber;
    private UUID operatorId;
    private String operatorName;
    private UUID assignedBusId;
    private String assignedBusNumber;
    private boolean isApproved;
    private UUID approvedBy;
    private Instant approvedAt;
    private Instant createdAt;
}