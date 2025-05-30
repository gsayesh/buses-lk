package lk.buses.user.application.dto.request;

import jakarta.validation.constraints.*;
import lk.buses.common.core.enums.LanguageCode;
import lombok.Data;

// User Registration Request
@Data
public class UserRegistrationRequest {
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^(\\+94|0)?[0-9]{9}$", message = "Invalid Sri Lankan mobile number")
    private String mobileNumber;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    private String lastName;

    private LanguageCode preferredLanguage = LanguageCode.EN;
}

// Login Request
@Data
public class LoginRequest {
    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    @NotBlank(message = "Password is required")
    private String password;
}

// OTP Verification Request
@Data
public class OtpVerificationRequest {
    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    @NotBlank(message = "OTP is required")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits")
    private String otp;
}

// Driver Registration Request
@Data
public class DriverRegistrationRequest {
    @NotBlank(message = "License number is required")
    @Size(max = 50)
    private String licenseNumber;

    @NotNull(message = "Operator ID is required")
    private UUID operatorId;
}

// User Update Request
@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    private LanguageCode preferredLanguage;
}

// Change Password Request
@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;
}

// Driver Approval Request
@Data
public class DriverApprovalRequest {
    @NotNull(message = "Driver profile ID is required")
    private UUID driverProfileId;

    private String remarks;
}

// Bus Assignment Request
@Data
public class BusAssignmentRequest {
    @NotNull(message = "Driver profile ID is required")
    private UUID driverProfileId;

    @NotNull(message = "Bus ID is required")
    private UUID busId;
}