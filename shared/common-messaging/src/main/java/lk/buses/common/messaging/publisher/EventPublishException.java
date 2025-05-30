package lk.buses.common.messaging.publisher;

import lk.buses.common.core.exception.BusinessException;

/**
 * Exception thrown when event publishing fails
 */
public class EventPublishException extends BusinessException {

    public EventPublishException(String message) {
        super(message, "EVENT_PUBLISH_FAILED");
    }

    public EventPublishException(String message, Throwable cause) {
        super(message, "EVENT_PUBLISH_FAILED", cause);
    }
}