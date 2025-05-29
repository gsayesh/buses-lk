package lk.buses.common.core.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.Map;

@Data
@Builder
public class ErrorDetails {
    private String code;
    private String message;
    private String details;
    private Map<String, String> fieldErrors;
    @Builder.Default
    private Instant timestamp = Instant.now();
}