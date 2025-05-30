package lk.buses.common.security;

import lk.buses.common.core.exception.UnauthorizedException;

/**
 * Exception thrown when JWT token validation fails
 */
public class InvalidTokenException extends UnauthorizedException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message);
        this.initCause(cause);
    }
}