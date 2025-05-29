package lk.buses.route.domain.repository;

import lk.buses.route.domain.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RouteRepository extends JpaRepository<Route, UUID>, JpaSpecificationExecutor<Route> {

    Optional<Route> findByRouteNumber(String routeNumber);

    List<Route> findByIsActiveTrue();

    @Query("SELECT r FROM Route r WHERE r.originCity = :city OR r.destinationCity = :city")
    List<Route> findRoutesByCity(String city);

    @Query("SELECT r FROM Route r JOIN r.stops s WHERE LOWER(s.stopNameEn) = LOWER(:stopName)")
    List<Route> findRoutesPassingThrough(String stopName);

    @Query("SELECT DISTINCT r FROM Route r " +
            "JOIN r.stops s1 JOIN r.stops s2 " +
            "WHERE LOWER(s1.stopNameEn) = LOWER(:fromStop) " +
            "AND LOWER(s2.stopNameEn) = LOWER(:toStop) " +
            "AND s1.stopSequence < s2.stopSequence")
    List<Route> findRoutesBetweenStops(String fromStop, String toStop);
}
