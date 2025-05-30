package lk.buses.bus.domain.repository;

import lk.buses.bus.domain.entity.BusTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusTrackingRepository extends JpaRepository<BusTracking, UUID> {

    List<BusTracking> findByBusIdOrderByTimestampDesc(UUID busId);

    @Query("SELECT bt FROM BusTracking bt WHERE bt.bus.id = :busId " +
            "AND bt.timestamp > :since ORDER BY bt.timestamp DESC")
    List<BusTracking> findRecentTracking(UUID busId, Instant since);

    @Query("SELECT DISTINCT ON (bt.bus.id) bt FROM BusTracking bt " +
            "WHERE bt.timestamp > :since ORDER BY bt.bus.id, bt.timestamp DESC")
    List<BusTracking> findLatestTrackingForAllBuses(Instant since);

    @Query(value = "SELECT * FROM bus_tracking bt " +
            "WHERE ST_DWithin(bt.location, ST_MakePoint(:lng, :lat)::geography, :radiusMeters) " +
            "AND bt.timestamp > :since",
            nativeQuery = true)
    List<BusTracking> findBusesWithinRadius(double lat, double lng, double radiusMeters, Instant since);

    @Query("SELECT bt FROM BusTracking bt WHERE bt.bus.id = :busId " +
            "ORDER BY bt.timestamp DESC LIMIT 1")
    Optional<BusTracking> findLatestTracking(UUID busId);

    @Query("SELECT bt FROM BusTracking bt WHERE bt.speedKmh > :speedLimit " +
            "AND bt.timestamp > :since ORDER BY bt.timestamp DESC")
    List<BusTracking> findSpeedViolations(Double speedLimit, Instant since);
}