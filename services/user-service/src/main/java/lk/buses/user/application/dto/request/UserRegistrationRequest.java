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