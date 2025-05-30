package lk.buses.route.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "route_segment_times",
        uniqueConstraints = @UniqueConstraint(columnNames = {"route_id", "from_stop_id", "to_stop_id", "time_period"}),
        indexes = {
                @Index(name = "idx_segment_times_route", columnList = "route_id"),
                @Index(name = "idx_segment_times_stops", columnList = "from_stop_id, to_stop_id")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RouteSegmentTime {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_stop_id", nullable = false)
    private RouteStop fromStop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_stop_id", nullable = false)
    private RouteStop toStop;

    @Column(name = "time_period", nullable = false, length = 20)
    private String timePeriod; // day, night, peak, off_peak

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "distance_km", precision = 10, scale = 2)
    private BigDecimal distanceKm;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
