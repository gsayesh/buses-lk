package lk.buses.bus.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "service_categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ServiceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private lk.buses.common.core.enums.ServiceCategory category;

    @Column(name = "fare_multiplier", nullable = false, precision = 3, scale = 2)
    private Double fareMultiplier;

    @Column(name = "display_name_en", nullable = false, length = 50)
    private String displayNameEn;

    @Column(name = "display_name_si", nullable = false, length = 50)
    private String displayNameSi;

    @Column(name = "display_name_ta", nullable = false, length = 50)
    private String displayNameTa;

    @Column(columnDefinition = "jsonb")
    private String amenities;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}