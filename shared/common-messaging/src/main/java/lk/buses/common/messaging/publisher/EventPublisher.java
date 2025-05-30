package lk.buses.common.messaging.publisher;

import lk.buses.common.messaging.event.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(String exchange, String routingKey, BaseEvent event) {
        try {
            log.debug("Publishing event: {} to exchange: {} with routing key: {}",
                    event.getEventType(), exchange, routingKey);

            rabbitTemplate.convertAndSend(exchange, routingKey, event);

            log.debug("Event published successfully: {}", event.getEventId());
        } catch (Exception e) {
            log.error("Failed to publish event: {} to exchange: {}",
                    event.getEventType(), exchange, e);
            throw new EventPublishException("Failed to publish event", e);
        }
    }

    public void publish(String exchange, String routingKey, Object message) {
        try {
            log.debug("Publishing message to exchange: {} with routing key: {}",
                    exchange, routingKey);

            rabbitTemplate.convertAndSend(exchange, routingKey, message);

            log.debug("Message published successfully");
        } catch (Exception e) {
            log.error("Failed to publish message to exchange: {}", exchange, e);
            throw new EventPublishException("Failed to publish message", e);
        }
    }
}