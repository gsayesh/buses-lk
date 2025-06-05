package lk.buses.route.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "routes", indexes = {
        @Index(name = "idx_route_number", columnList = "route_number"),
        @Index(name = "idx_route_active", columnList = "is_active")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "route_number", unique = true, nullable = false, length = 20)
    private String routeNumber;

    @Column(name = "route_name_en", nullable = false, length = 200)
    private String routeNameEn;

    @Column(name = "route_name_si", nullable = false, length = 200)
    private String routeNameSi;

    @Column(name = "route_name_ta", nullable = false, length = 200)
    private String routeNameTa;

    @Column(name = "origin_city", nullable = false, length = 100)
    private String originCity;

    @Column(name = "destination_city", nullable = false, length = 100)
    private String destinationCity;

    @Column(name = "total_distance_km")
    private Double totalDistanceKm;

    @Column(name = "is_rotational")
    private boolean isRotational;

    @Column(name = "route_photo_url")
    private String routePhotoUrl;

    @Column(name = "is_active")
    private boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("stopSequence ASC")
    @Builder.Default
    private List<RouteStop> stops = new ArrayList<>();

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RouteSegmentTime> segmentTimes = new ArrayList<>();
}