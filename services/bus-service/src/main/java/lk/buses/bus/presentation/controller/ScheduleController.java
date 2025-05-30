package lk.buses.bus.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.buses.bus.application.dto.request.CreateScheduleRequest;
import lk.buses.bus.application.dto.response.BusScheduleResponse;
import lk.buses.bus.application.service.ScheduleApplicationService;
import lk.buses.common.core.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedules", description = "Bus schedule management APIs")
public class ScheduleController {

    private final ScheduleApplicationService scheduleService;

    @GetMapping("/route/{routeId}")
    @Operation(summary = "Get schedules for route")
    public ResponseEntity<BaseResponse<List<BusScheduleResponse>>> getSchedulesForRoute(
            @PathVariable UUID routeId) {
        List<BusScheduleResponse> schedules = scheduleService.getSchedulesForRoute(routeId);
        return ResponseEntity.ok(BaseResponse.success(schedules));
    }

    @GetMapping("/route/{routeId}/today")
    @Operation(summary = "Get today's schedules for route")
    public ResponseEntity<BaseResponse<List<BusScheduleResponse>>> getTodaySchedules(
            @PathVariable UUID routeId) {
        List<BusScheduleResponse> schedules = scheduleService.getTodaySchedules(routeId);
        return ResponseEntity.ok(BaseResponse.success(schedules));
    }

    @GetMapping("/route/{routeId}/next")
    @Operation(summary = "Get next departures for route")
    public ResponseEntity<BaseResponse<List<BusScheduleResponse>>> getNextDepartures(
            @PathVariable UUID routeId,
            @RequestParam(defaultValue = "5") int limit) {
        List<BusScheduleResponse> schedules = scheduleService.getNextDepartures(routeId, limit);
        return ResponseEntity.ok(BaseResponse.success(schedules));
    }

    @PostMapping
    @Operation(summary = "Create new schedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BaseResponse<BusScheduleResponse>> createSchedule(
            @Valid @RequestBody CreateScheduleRequest request) {
        BusScheduleResponse schedule = scheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(schedule, "Schedule created successfully"));
    }
}