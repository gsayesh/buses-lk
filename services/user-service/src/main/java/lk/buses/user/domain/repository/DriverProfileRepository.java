package lk.buses.user.domain.repository;

import lk.buses.user.domain.entity.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriverProfileRepository extends JpaRepository<DriverProfile, UUID>,
        JpaSpecificationExecutor<DriverProfile> {

    Optional<DriverProfile> findByUserId(UUID userId);

    Optional<DriverProfile> findByLicenseNumber(String licenseNumber);

    List<DriverProfile> findByOperatorId(UUID operatorId);

    List<DriverProfile> findByAssignedBusId(UUID busId);

    @Query("SELECT dp FROM DriverProfile dp WHERE dp.isApproved = false")
    List<DriverProfile> findPendingApprovals();

    @Query("SELECT dp FROM DriverProfile dp WHERE dp.operatorId = :operatorId AND dp.isApproved = true")
    List<DriverProfile> findApprovedDriversByOperator(UUID operatorId);
}
