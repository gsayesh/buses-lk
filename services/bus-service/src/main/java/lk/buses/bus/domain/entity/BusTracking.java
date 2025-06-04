package lk.buses.bus.domain.entity;

import jakarta.persistence.*;
import lk.buses.common.core.enums.TrackingSource;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bus_tracking", indexes = {
        @Index(name = "idx_tracking_bus_timestamp", columnList = "bus_id, timestamp DESC"),
        @Index(name = "idx_tracking_timestamp", columnList = "timestamp"),
        @Index(name = "idx_tracking_location", columnList = "location")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BusTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @Column(name = "driver_id")
    private UUID driverId;

    @Column(name = "tracking_source", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrackingSource trackingSource;

    @Column(nullable = false, precision = 10, scale = 8)
    private Double latitude;

    @Column(nullable = false, precision = 11, scale = 8)
    private Double longitude;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point location;

    @Column(name = "speed_kmh")
    private Double speedKmh;  // Removed precision and scale

    @Column
    private Double heading;  // Removed precision and scale

    @Column(nullable = false)
    private Instant timestamp;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}