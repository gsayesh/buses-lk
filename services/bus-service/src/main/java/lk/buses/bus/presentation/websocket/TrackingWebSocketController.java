package lk.buses.bus.presentation.websocket;

import lk.buses.bus.application.dto.request.LocationUpdateRequest;
import lk.buses.bus.application.dto.response.BusLocationResponse;
import lk.buses.bus.application.service.TrackingApplicationService;
import lk.buses.common.core.enums.TrackingSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TrackingWebSocketController {

    private final TrackingApplicationService trackingService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/bus/location")
    @SendTo("/topic/bus-locations")
    public BusLocationResponse handleLocationUpdate(LocationUpdateMessage message) {
        log.debug("Received WebSocket location update for bus: {}", message.getBusId());

        // Create request using constructor and setters instead of builder
        LocationUpdateRequest request = new LocationUpdateRequest();
        request.setLatitude(message.getLatitude());
        request.setLongitude(message.getLongitude());
        request.setSpeed(message.getSpeed());
        request.setHeading(message.getHeading());
        request.setDriverId(message.getDriverId());
        request.setTrackingSource(message.getTrackingSource());

        trackingService.updateBusLocation(message.getBusId(), request);

        return trackingService.getCurrentLocation(message.getBusId());
    }

    public void broadcastLocationUpdate(String routeId, BusLocationResponse location) {
        messagingTemplate.convertAndSend("/topic/route/" + routeId + "/buses", location);
    }

    @lombok.Data
    public static class LocationUpdateMessage {
        private UUID busId;
        private UUID driverId;
        private Double latitude;
        private Double longitude;
        private Double speed;
        private Double heading;
        private TrackingSource trackingSource;
    }
}