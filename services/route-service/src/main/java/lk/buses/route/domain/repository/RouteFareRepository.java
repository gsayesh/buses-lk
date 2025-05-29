package lk.buses.route.domain.repository;

import lk.buses.route.domain.entity.RouteFare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RouteFareRepository extends JpaRepository<RouteFare, UUID> {

    Optional<RouteFare> findByRouteIdAndFromStopIdAndToStopIdAndFareStructureId(
            UUID routeId, UUID fromStopId, UUID toStopId, UUID fareStructureId);

    List<RouteFare> findByFareStructureId(UUID fareStructureId);

    List<RouteFare> findByRouteId(UUID routeId);

    @Query("SELECT rf FROM RouteFare rf " +
            "WHERE rf.route.id = :routeId " +
            "AND rf.fareStructure.isActive = true")
    List<RouteFare> findActiveRouteFares(UUID routeId);
}