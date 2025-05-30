package lk.buses.bus.application.dto.request;

import jakarta.validation.constraints.*;
import lk.buses.common.core.enums.BusOperatorType;
import lombok.Data;

@Data
public class CreateOperatorRequest {
    @NotBlank(message = "Operator code is required")
    @Size(max = 20)
    private String operatorCode;

    @NotBlank(message = "Operator name is required")
    @Size(max = 100)
    private String operatorName;

    @NotNull(message = "Operator type is required")
    private BusOperatorType operatorType;

    @Pattern(regexp = "^(\\+94|0)?[0-9]{9}$", message = "Invalid Sri Lankan phone number")
    private String contactNumber;

    @Email(message = "Invalid email format")
    @Size(max = 100)
    private String email;

    private String address;
}
