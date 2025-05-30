package lk.buses.user.application.dto.response;

import lk.buses.common.core.enums.UserRole;
import lk.buses.common.core.enums.LanguageCode;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class OtpResponse {
    private boolean sent;
    private String message;
    private Integer expiryMinutes;
}