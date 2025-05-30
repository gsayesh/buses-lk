package lk.buses.user.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

// OTP Verification Request
@Data
public class OtpVerificationRequest {
    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    @NotBlank(message = "OTP is required")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits")
    private String otp;
}