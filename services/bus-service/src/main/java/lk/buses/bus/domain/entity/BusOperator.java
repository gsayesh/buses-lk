package lk.buses.bus.domain.entity;

import jakarta.persistence.*;
import lk.buses.common.core.enums.BusOperatorType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bus_operators", indexes = {
        @Index(name = "idx_operator_code", columnList = "operator_code"),
        @Index(name = "idx_operator_type", columnList = "operator_type")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BusOperator {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "operator_code", unique = true, nullable = false, length = 20)
    private String operatorCode;

    @Column(name = "operator_name", nullable = false, length = 100)
    private String operatorName;

    @Column(name = "operator_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BusOperatorType operatorType;

    @Column(name = "contact_number", length = 20)
    private String contactNumber;

    @Column(length = 100)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "is_active")
    private boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "operator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Bus> buses = new ArrayList<>();
}