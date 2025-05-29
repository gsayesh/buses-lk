package lk.buses.common.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private ErrorDetails error;
    private Metadata metadata;
    @Builder.Default
    private Instant timestamp = Instant.now();

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> success(T data, String message) {
        return BaseResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> error(String message, String code) {
        return BaseResponse.<T>builder()
                .success(false)
                .error(ErrorDetails.builder()
                        .message(message)
                        .code(code)
                        .build())
                .build();
    }
}