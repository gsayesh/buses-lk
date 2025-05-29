package lk.buses.common.core.exception;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED");
    }
}