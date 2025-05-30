package lk.buses.bus.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.buses.bus.application.dto.request.*;
import lk.buses.bus.application.dto.response.*;
import lk.buses.bus.application.service.BusApplicationService;
import lk.buses.common.core.dto.BaseResponse;
import lk.buses.common.core.dto.PageableRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/buses")
@RequiredArgsConstructor
@Tag(name = "Buses", description = "Bus management APIs")
public class BusController {

    private final BusApplicationService busService;

    @GetMapping
    @Operation(summary = "Search buses")
    public ResponseEntity<BaseResponse<Page<BusResponse>>> searchBuses(
            @Valid BusSearchRequest searchRequest,
            @Valid PageableRequest pageRequest) {
        Page<BusResponse> buses = busService.searchBuses(searchRequest, pageRequest);
        return ResponseEntity.ok(BaseResponse.success(buses));
    }

    @GetMapping("/{busId}")
    @Operation(summary = "Get bus by ID")
    public ResponseEntity<BaseResponse<BusResponse>> getBusById(@PathVariable UUID busId) {
        BusResponse bus = busService.getBusById(busId);
        return ResponseEntity.ok(BaseResponse.success(bus));
    }

    @GetMapping("/registration/{registrationNumber}")
    @Operation(summary = "Get bus by registration number")
    public ResponseEntity<BaseResponse<BusResponse>> getBusByRegistration(
            @PathVariable String registrationNumber) {
        BusResponse bus = busService.getBusByRegistrationNumber(registrationNumber);
        return ResponseEntity.ok(BaseResponse.success(bus));
    }

    @PostMapping
    @Operation(summary = "Register new bus")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BaseResponse<BusResponse>> registerBus(
            @Valid @RequestBody RegisterBusRequest request) {
        BusResponse bus = busService.registerBus(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(bus, "Bus registered successfully"));
    }

    @PostMapping("/gps-device")
    @Operation(summary = "Assign GPS device to bus")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BaseResponse<BusResponse>> assignGpsDevice(
            @Valid @RequestBody GpsDeviceAssignmentRequest request) {
        BusResponse bus = busService.assignGpsDevice(request);
        return ResponseEntity.ok(BaseResponse.success(bus, "GPS device assigned successfully"));
    }

    @DeleteMapping("/{busId}")
    @Operation(summary = "Deactivate bus")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BaseResponse<Void>> deactivateBus(@PathVariable UUID busId) {
        busService.deactivateBus(busId);
        return ResponseEntity.ok(BaseResponse.success(null, "Bus deactivated successfully"));
    }
}