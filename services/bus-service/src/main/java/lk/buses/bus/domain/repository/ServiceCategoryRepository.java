package lk.buses.bus.domain.repository;

import lk.buses.bus.domain.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, UUID> {

    Optional<ServiceCategory> findByCategory(lk.buses.common.core.enums.ServiceCategory category);
}