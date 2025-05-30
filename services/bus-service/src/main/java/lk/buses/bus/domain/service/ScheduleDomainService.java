package lk.buses.bus.domain.service;

import lk.buses.bus.domain.entity.BusSchedule;
import lk.buses.bus.domain.repository.BusRepository;
import lk.buses.bus.domain.repository.BusScheduleRepository;
import lk.buses.common.core.exception.BusinessException;
import lk.buses.common.core.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleDomainService {

    private final BusScheduleRepository scheduleRepository;
    private final BusRepository busRepository;

    @Transactional
    public BusSchedule createSchedule(BusSchedule schedule) {
        log.debug("Creating schedule for route: {}", schedule.getRouteId());

        // Validate bus exists if not rotational
        if (schedule.getBus() != null) {
            busRepository.findById(schedule.getBus().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bus", schedule.getBus().getId().toString()));
        }

        // Validate time logic
        if (schedule.getArrivalTime().isBefore(schedule.getDepartureTime())) {
            throw new BusinessException("Arrival time cannot be before departure time", "INVALID_TIMES");
        }

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public BusSchedule updateSchedule(UUID scheduleId, BusSchedule updates) {
        log.debug("Updating schedule: {}", scheduleId);

        BusSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule", scheduleId.toString()));

        // Update allowed fields
        if (updates.getDepartureTime() != null) {
            schedule.setDepartureTime(updates.getDepartureTime());
        }
        if (updates.getArrivalTime() != null) {
            schedule.setArrivalTime(updates.getArrivalTime());
        }
        if (updates.getDaysOfWeek() != null) {
            schedule.setDaysOfWeek(updates.getDaysOfWeek());
        }

        // Validate time logic
        if (schedule.getArrivalTime().isBefore(schedule.getDepartureTime())) {
            throw new BusinessException("Arrival time cannot be before departure time", "INVALID_TIMES");
        }

        return scheduleRepository.save(schedule);
    }

    public List<BusSchedule> getSchedulesForRoute(UUID routeId) {
        return scheduleRepository.findActiveByRoute(routeId);
    }

    public List<BusSchedule> getSchedulesForToday(UUID routeId) {
        int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        return scheduleRepository.findByDayOfWeek(dayOfWeek).stream()
                .filter(s -> s.getRouteId().equals(routeId))
                .toList();
    }

    public List<BusSchedule> getNextDepartures(UUID routeId, int limit) {
        LocalTime currentTime = LocalTime.now();
        int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();

        List<BusSchedule> schedules = scheduleRepository.findNextDepartures(
                routeId, currentTime, dayOfWeek
        );

        return schedules.stream()
                .limit(limit)
                .toList();
    }
}