package lk.buses.route.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "fare_structures", indexes = {
        @Index(name = "idx_fare_structure_active", columnList = "is_active"),
        @Index(name = "idx_fare_structure_date", columnList = "effective_date")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FareStructure {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "minimum_fare", nullable = false, precision = 10, scale = 2)
    private BigDecimal minimumFare;

    @Column(name = "fare_per_km", nullable = false, precision = 10, scale = 4)
    private BigDecimal farePerKm;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_active")
    private boolean isActive;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}