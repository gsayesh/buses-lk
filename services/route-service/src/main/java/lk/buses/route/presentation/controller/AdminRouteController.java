package lk.buses.route.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.buses.common.core.dto.BaseResponse;
import lk.buses.route.application.dto.request.*;
import lk.buses.route.application.dto.response.*;
import lk.buses.route.application.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/routes")
@RequiredArgsConstructor
@Tag(name = "Admin Routes", description = "Route administration APIs")
@SecurityRequirement(name = "bearerAuth")
public class AdminRouteController {

    private final RouteApplicationService routeService;
    private final GovernmentFareService governmentFareService;

    @PostMapping
    @Operation(summary = "Create new route")
    @PreAuthorize("hasAnyRole('ADMIN', 'GOVERNMENT')")
    public ResponseEntity<BaseResponse<RouteResponse>> createRoute(
            @Valid @RequestBody CreateRouteRequest request) {
        RouteResponse route = routeService.createRoute(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(route, "Route created successfully"));
    }

    @PostMapping("/{routeId}/stops")
    @Operation(summary = "Add stop to route")
    @PreAuthorize("hasAnyRole('ADMIN', 'GOVERNMENT')")
    public ResponseEntity<BaseResponse<Void>> addStopToRoute(
            @PathVariable UUID routeId,
            @Valid @RequestBody CreateRouteStopRequest request) {
        routeService.addStopToRoute(routeId, request);
        return ResponseEntity.ok(BaseResponse.success(null, "Stop added successfully"));
    }

    @PostMapping("/fares/update")
    @Operation(summary = "Update nationwide fares")
    @PreAuthorize("hasRole('GOVERNMENT')")
    public ResponseEntity<BaseResponse<FareStructureResponse>> updateNationwideFares(
            @Valid @RequestBody GovernmentFareUpdateRequest request) {
        FareStructureResponse response = governmentFareService.updateNationwideFares(request);
        return ResponseEntity.ok(BaseResponse.success(response, "Fare update scheduled successfully"));
    }

    @GetMapping("/fares/history")
    @Operation(summary = "Get fare structure history")
    @PreAuthorize("hasAnyRole('ADMIN', 'GOVERNMENT')")
    public ResponseEntity<BaseResponse<List<FareStructureResponse>>> getFareHistory() {
        List<FareStructureResponse> history = governmentFareService.getFareHistory();
        return ResponseEntity.ok(BaseResponse.success(history));
    }
}