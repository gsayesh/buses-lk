package lk.buses.bus.application.service;

import lk.buses.bus.application.dto.request.CreateScheduleRequest;
import lk.buses.bus.application.dto.response.BusScheduleResponse;
import lk.buses.bus.application.mapper.ScheduleMapper;
import lk.buses.bus.domain.entity.Bus;
import lk.buses.bus.domain.entity.BusSchedule;
import lk.buses.bus.domain.repository.BusRepository;
import lk.buses.bus.domain.service.ScheduleDomainService;
import lk.buses.bus.infrastructure.external.RouteServiceClient;
import lk.buses.common.core.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleApplicationService {

    private final BusRepository busRepository;
    private final ScheduleDomainService scheduleDomainService;
    private final ScheduleMapper scheduleMapper;
    private final RouteServiceClient routeServiceClient;

    @Transactional
    public BusScheduleResponse createSchedule(CreateScheduleRequest request) {
        log.info("Creating schedule for route: {}", request.getRouteId());

        BusSchedule schedule = scheduleMapper.toEntity(request);

        // Set bus if provided (not rotational)
        if (request.getBusId() != null) {
            Bus bus = busRepository.findById(request.getBusId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bus", request.getBusId().toString()));
            schedule.setBus(bus);
        }

        schedule = scheduleDomainService.createSchedule(schedule);

        // Enrich with route information
        BusScheduleResponse response = scheduleMapper.toResponse(schedule);
        enrichWithRouteInfo(response);

        log.info("Schedule created successfully: {}", schedule.getId());
        return response;
    }

    public List<BusScheduleResponse> getSchedulesForRoute(UUID routeId) {
        log.debug("Getting schedules for route: {}", routeId);

        List<BusSchedule> schedules = scheduleDomainService.getSchedulesForRoute(routeId);

        return schedules.stream()
                .map(schedule -> {
                    BusScheduleResponse response = scheduleMapper.toResponse(schedule);
                    enrichWithRouteInfo(response);
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<BusScheduleResponse> getTodaySchedules(UUID routeId) {
        log.debug("Getting today's schedules for route: {}", routeId);

        List<BusSchedule> schedules = scheduleDomainService.getSchedulesForToday(routeId);

        return schedules.stream()
                .map(schedule -> {
                    BusScheduleResponse response = scheduleMapper.toResponse(schedule);
                    enrichWithRouteInfo(response);
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<BusScheduleResponse> getNextDepartures(UUID routeId, int limit) {
        log.debug("Getting next {} departures for route: {}", limit, routeId);

        List<BusSchedule> schedules = scheduleDomainService.getNextDepartures(routeId, limit);

        return schedules.stream()
                .map(schedule -> {
                    BusScheduleResponse response = scheduleMapper.toResponse(schedule);
                    enrichWithRouteInfo(response);
                    return response;
                })
                .collect(Collectors.toList());
    }

    private void enrichWithRouteInfo(BusScheduleResponse response) {
        try {
            var routeInfo = routeServiceClient.getRoute(response.getRouteId());
            response.setRouteNumber(routeInfo.getRouteNumber());
            response.setRouteName(routeInfo.getRouteNameEn());
        } catch (Exception e) {
            log.warn("Failed to fetch route info for schedule: {}", response.getId(), e);
        }
    }
}