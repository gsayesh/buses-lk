package lk.buses.user.infrastructure.messaging;

import lk.buses.common.messaging.event.UserRegisteredEvent;
import lk.buses.common.messaging.publisher.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static lk.buses.common.messaging.config.RabbitMQConfig.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserEventPublisher {

    private final EventPublisher eventPublisher;

    public void publishUserRegistered(UserRegisteredEvent event) {
        log.info("Publishing user registered event for user: {}", event.getUserId());
        eventPublisher.publish(USER_EXCHANGE, USER_REGISTERED_KEY, event);
    }
}
