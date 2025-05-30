package lk.buses.user.application.dto.request;

import jakarta.validation.constraints.*;
import lk.buses.common.core.enums.LanguageCode;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;
}