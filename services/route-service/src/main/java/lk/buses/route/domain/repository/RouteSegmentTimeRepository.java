package lk.buses.route.domain.repository;

import lk.buses.route.domain.entity.RouteSegmentTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RouteSegmentTimeRepository extends JpaRepository<RouteSegmentTime, UUID> {

    List<RouteSegmentTime> findByRouteIdAndTimePeriod(UUID routeId, String timePeriod);

    Optional<RouteSegmentTime> findByRouteIdAndFromStopIdAndToStopIdAndTimePeriod(
            UUID routeId, UUID fromStopId, UUID toStopId, String timePeriod);

    @Query("SELECT rst FROM RouteSegmentTime rst " +
            "JOIN rst.fromStop fs JOIN rst.toStop ts " +
            "WHERE rst.route.id = :routeId " +
            "AND rst.timePeriod = :timePeriod " +
            "AND fs.stopSequence >= (SELECT stopSequence FROM RouteStop WHERE id = :fromStopId) " +
            "AND ts.stopSequence <= (SELECT stopSequence FROM RouteStop WHERE id = :toStopId) " +
            "ORDER BY fs.stopSequence")
    List<RouteSegmentTime> findSegmentsBetweenStops(
            UUID routeId, UUID fromStopId, UUID toStopId, String timePeriod);
}