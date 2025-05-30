package lk.buses.route.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "route_stops",
        uniqueConstraints = @UniqueConstraint(columnNames = {"route_id", "stop_sequence"}),
        indexes = {
                @Index(name = "idx_route_stops_route", columnList = "route_id"),
                @Index(name = "idx_route_stops_location", columnList = "location")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RouteStop {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "stop_sequence", nullable = false)
    private Integer stopSequence;

    @Column(name = "stop_name_en", nullable = false, length = 100)
    private String stopNameEn;

    @Column(name = "stop_name_si", nullable = false, length = 100)
    private String stopNameSi;

    @Column(name = "stop_name_ta", nullable = false, length = 100)
    private String stopNameTa;

    @Column(precision = 10, scale = 8)
    private Double latitude;

    @Column(precision = 11, scale = 8)
    private Double longitude;

    @Column(columnDefinition = "geography(Point, 4326)")
    private Point location;

    @Column(name = "is_major_stop")
    private boolean isMajorStop;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
