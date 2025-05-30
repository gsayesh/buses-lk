package lk.buses.route.domain.repository;

import lk.buses.route.domain.entity.RouteStop;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, UUID> {

    List<RouteStop> findByRouteIdOrderByStopSequence(UUID routeId);

    Optional<RouteStop> findByRouteIdAndStopSequence(UUID routeId, Integer stopSequence);

    @Query(value = "SELECT * FROM route_stops " +
            "WHERE ST_DWithin(location, ST_MakePoint(:lng, :lat)::geography, :radiusMeters) " +
            "ORDER BY ST_Distance(location, ST_MakePoint(:lng, :lat)::geography)",
            nativeQuery = true)
    List<RouteStop> findNearbyStops(double lat, double lng, double radiusMeters);

    @Query("SELECT rs FROM RouteStop rs WHERE rs.isMajorStop = true ORDER BY rs.stopNameEn")
    List<RouteStop> findMajorStops();
}
