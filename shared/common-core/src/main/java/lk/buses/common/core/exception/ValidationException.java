package lk.buses.common.core.exception;

import java.util.Map;
import lombok.Getter;

@Getter
public class ValidationException extends BusinessException {
    private final Map<String, String> fieldErrors;

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message, "VALIDATION_ERROR");
        this.fieldErrors = fieldErrors;
    }
}