package lk.buses.bus.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.buses.bus.application.dto.request.LocationUpdateRequest;
import lk.buses.bus.application.dto.response.*;
import lk.buses.bus.application.service.TrackingApplicationService;
import lk.buses.common.core.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tracking")
@RequiredArgsConstructor
@Tag(name = "Tracking", description = "Bus tracking APIs")
public class TrackingController {

    private final TrackingApplicationService trackingService;

    @PutMapping("/bus/{busId}/location")
    @Operation(summary = "Update bus location")
    @PreAuthorize("hasRole('DRIVER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BaseResponse<Void>> updateLocation(
            @PathVariable UUID busId,
            @Valid @RequestBody LocationUpdateRequest request) {
        trackingService.updateBusLocation(busId, request);
        return ResponseEntity.ok(BaseResponse.success(null, "Location updated successfully"));
    }

    @GetMapping("/bus/{busId}/location")
    @Operation(summary = "Get current bus location")
    public ResponseEntity<BaseResponse<BusLocationResponse>> getCurrentLocation(
            @PathVariable UUID busId) {
        BusLocationResponse location = trackingService.getCurrentLocation(busId);
        return ResponseEntity.ok(BaseResponse.success(location));
    }

    @GetMapping("/nearby")
    @Operation(summary = "Find nearby buses")
    public ResponseEntity<BaseResponse<NearbyBusesResponse>> findNearbyBuses(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5.0") double radiusKm) {
        NearbyBusesResponse nearbyBuses = trackingService.findNearbyBuses(latitude, longitude, radiusKm);
        return ResponseEntity.ok(BaseResponse.success(nearbyBuses));
    }

    @GetMapping("/bus/{busId}/history")
    @Operation(summary = "Get bus tracking history")
    public ResponseEntity<BaseResponse<TrackingHistoryResponse>> getTrackingHistory(
            @PathVariable UUID busId,
            @RequestParam(defaultValue = "1") int hours) {
        TrackingHistoryResponse history = trackingService.getBusTrackingHistory(
                busId, Duration.ofHours(hours)
        );
        return ResponseEntity.ok(BaseResponse.success(history));
    }
}