package lk.buses.user.domain.repository;

import lk.buses.user.domain.entity.User;
import lk.buses.common.core.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByMobileNumber(String mobileNumber);

    Optional<User> findByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);

    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.role = :role")
    List<User> findActiveUsersByRole(UserRole role);

    @Query("SELECT u FROM User u WHERE u.isVerified = false AND u.createdAt < :cutoffTime")
    List<User> findUnverifiedUsersOlderThan(Instant cutoffTime);
}