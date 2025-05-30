package lk.buses.bus.domain.entity;

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
@Table(name = "buses", indexes = {
        @Index(name = "idx_registration_number", columnList = "registration_number"),
        @Index(name = "idx_operator_id", columnList = "operator_id"),
        @Index(name = "idx_bus_active", columnList = "is_active")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "registration_number", unique = true, nullable = false, length = 20)
    private String registrationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id", nullable = false)
    private BusOperator operator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_category_id", nullable = false)
    private ServiceCategory serviceCategory;

    @Column(length = 50)
    private String make;

    @Column(length = 50)
    private String model;

    @Column(name = "year_of_manufacture")
    private Integer yearOfManufacture;

    @Column(name = "seating_capacity", nullable = false)
    private Integer seatingCapacity;

    @Column(name = "has_gps_device")
    private boolean hasGpsDevice;

    @Column(name = "gps_device_id", length = 100)
    private String gpsDeviceId;

    @Column(name = "bus_photo_url")
    private String busPhotoUrl;

    @Column(name = "is_active")
    private boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<BusSchedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<BusTracking> trackingHistory = new ArrayList<>();
}