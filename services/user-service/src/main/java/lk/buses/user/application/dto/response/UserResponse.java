package lk.buses.user.application.dto.response;

import lk.buses.common.core.enums.UserRole;
import lk.buses.common.core.enums.LanguageCode;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID id;
    private String mobileNumber;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private LanguageCode preferredLanguage;
    private boolean isVerified;
    private boolean isActive;
    private Instant createdAt;
}