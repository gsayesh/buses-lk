package lk.buses.common.messaging.event;

import lk.buses.common.core.enums.TrackingSource;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusLocationUpdatedEvent extends BaseEvent {
    private UUID busId;
    private UUID driverId;
    private double latitude;
    private double longitude;
    private double speed;
    private double heading;
    private TrackingSource trackingSource;

    @Builder
    public BusLocationUpdatedEvent(UUID busId, UUID driverId, double latitude,
                                   double longitude, double speed, double heading,
                                   TrackingSource trackingSource) {
        super("BUS_LOCATION_UPDATED", "bus-service");
        this.busId = busId;
        this.driverId = driverId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.heading = heading;
        this.trackingSource = trackingSource;
    }
}