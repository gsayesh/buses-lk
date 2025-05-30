package lk.buses.bus.domain.repository;

import lk.buses.bus.domain.entity.Bus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusRepository extends JpaRepository<Bus, UUID>, JpaSpecificationExecutor<Bus> {

    Optional<Bus> findByRegistrationNumber(String registrationNumber);

    List<Bus> findByOperatorId(UUID operatorId);

    List<Bus> findByServiceCategoryId(UUID serviceCategoryId);

    Page<Bus> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT b FROM Bus b WHERE b.operator.id = :operatorId AND b.isActive = true")
    List<Bus> findActiveByOperator(UUID operatorId);

    @Query("SELECT b FROM Bus b WHERE b.seatingCapacity >= :minCapacity")
    List<Bus> findByMinimumCapacity(Integer minCapacity);

    @Query("SELECT b FROM Bus b WHERE b.hasGpsDevice = true AND b.isActive = true")
    List<Bus> findGpsEnabledBuses();
}