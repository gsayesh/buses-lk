package lk.buses.common.messaging.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class BaseEvent {
    private UUID eventId;
    private String eventType;
    private Instant timestamp;
    private String source;

    protected BaseEvent(String eventType, String source) {
        this.eventId = UUID.randomUUID();
        this.eventType = eventType;
        this.timestamp = Instant.now();
        this.source = source;
    }
}