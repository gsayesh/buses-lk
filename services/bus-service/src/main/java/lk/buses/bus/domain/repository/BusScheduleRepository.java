package lk.buses.bus.domain.repository;

import lk.buses.bus.domain.entity.BusSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BusScheduleRepository extends JpaRepository<BusSchedule, UUID> {

    List<BusSchedule> findByRouteId(UUID routeId);

    List<BusSchedule> findByBusId(UUID busId);

    @Query("SELECT s FROM BusSchedule s WHERE s.routeId = :routeId AND s.isActive = true")
    List<BusSchedule> findActiveByRoute(UUID routeId);

    @Query("SELECT s FROM BusSchedule s WHERE :dayOfWeek MEMBER OF s.daysOfWeek AND s.isActive = true")
    List<BusSchedule> findByDayOfWeek(Integer dayOfWeek);

    @Query("SELECT s FROM BusSchedule s WHERE s.departureTime BETWEEN :startTime AND :endTime " +
            "AND :dayOfWeek MEMBER OF s.daysOfWeek AND s.isActive = true")
    List<BusSchedule> findByTimeRangeAndDay(LocalTime startTime, LocalTime endTime, Integer dayOfWeek);

    @Query("SELECT s FROM BusSchedule s WHERE s.routeId = :routeId " +
            "AND s.departureTime >= :afterTime AND :dayOfWeek MEMBER OF s.daysOfWeek " +
            "AND s.isActive = true ORDER BY s.departureTime")
    List<BusSchedule> findNextDepartures(UUID routeId, LocalTime afterTime, Integer dayOfWeek);
}