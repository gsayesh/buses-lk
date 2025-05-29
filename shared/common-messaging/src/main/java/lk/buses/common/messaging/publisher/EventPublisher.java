package lk.buses.common.messaging.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchanges
    public static final String USER_EXCHANGE = "buses.user.exchange";
    public static final String BUS_EXCHANGE = "buses.bus.exchange";
    public static final String BOOKING_EXCHANGE = "buses.booking.exchange";
    public static final String NOTIFICATION_EXCHANGE = "buses.notification.exchange";

    // Queues
    public static final String USER_NOTIFICATION_QUEUE = "buses.user.notification.queue";
    public static final String BOOKING_NOTIFICATION_QUEUE = "buses.booking.notification.queue";
    public static final String ANALYTICS_QUEUE = "buses.analytics.queue";

    // Routing Keys
    public static final String USER_REGISTERED_KEY = "user.registered";
    public static final String BOOKING_CREATED_KEY = "booking.created";
    public static final String BUS_LOCATION_KEY = "bus.location";

    @Bean
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    // Exchanges
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public TopicExchange busExchange() {
        return new TopicExchange(BUS_EXCHANGE);
    }

    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange(BOOKING_EXCHANGE);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    // Queues
    @Bean
    public Queue userNotificationQueue() {
        return QueueBuilder.durable(USER_NOTIFICATION_QUEUE)
                .withArgument("x-message-ttl", 86400000) // 24 hours
                .build();
    }

    @Bean
    public Queue bookingNotificationQueue() {
        return QueueBuilder.durable(BOOKING_NOTIFICATION_QUEUE)
                .withArgument("x-message-ttl", 86400000) // 24 hours
                .build();
    }

    @Bean
    public Queue analyticsQueue() {
        return QueueBuilder.durable(ANALYTICS_QUEUE)
                .withArgument("x-message-ttl", 604800000) // 7 days
                .build();
    }

    // Bindings
    @Bean
    public Binding userNotificationBinding() {
        return BindingBuilder
                .bind(userNotificationQueue())
                .to(userExchange())
                .with(USER_REGISTERED_KEY);
    }

    @Bean
    public Binding bookingNotificationBinding() {
        return BindingBuilder
                .bind(bookingNotificationQueue())
                .to(bookingExchange())
                .with(BOOKING_CREATED_KEY);
    }
}