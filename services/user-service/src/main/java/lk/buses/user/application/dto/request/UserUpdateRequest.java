package lk.buses.user.application.dto.request;

import jakarta.validation.constraints.*;
import lk.buses.common.core.enums.LanguageCode;
import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    private LanguageCode preferredLanguage;
}