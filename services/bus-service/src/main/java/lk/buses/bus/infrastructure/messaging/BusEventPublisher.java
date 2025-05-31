package lk.buses.bus.infrastructure.messaging;

import lk.buses.common.messaging.event.BusLocationUpdatedEvent;
import lk.buses.common.messaging.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static lk.buses.common.messaging.config.RabbitMQConfig.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class BusEventPublisher {

    private final EventPublisher eventPublisher;

    public void publishLocationUpdate(BusLocationUpdatedEvent event) {
        log.debug("Publishing bus location update for bus: {}", event.getBusId());
        eventPublisher.publish(BUS_EXCHANGE, BUS_LOCATION_KEY, event);
    }
}