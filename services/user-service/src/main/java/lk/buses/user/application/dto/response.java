package lk.buses.user.application.dto.response;

import lk.buses.common.core.enums.UserRole;
import lk.buses.common.core.enums.LanguageCode;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

// User Response
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

// Authentication Response
@Data
@Builder
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserResponse user;
}

// Driver Profile Response
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

// OTP Response
@Data
@Builder
public class OtpResponse {
    private boolean sent;
    private String message;
    private Integer expiryMinutes;
}