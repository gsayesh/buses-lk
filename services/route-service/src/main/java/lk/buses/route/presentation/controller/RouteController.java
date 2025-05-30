package lk.buses.route.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.buses.common.core.dto.BaseResponse;
import lk.buses.common.core.dto.PageableRequest;
import lk.buses.route.application.dto.request.*;
import lk.buses.route.application.dto.response.*;
import lk.buses.route.application.service.RouteApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
@Tag(name = "Routes", description = "Route management APIs")
public class RouteController {

    private final RouteApplicationService routeService;

    @GetMapping
    @Operation(summary = "Get all routes")
    public ResponseEntity<BaseResponse<Page<RouteResponse>>> getAllRoutes(
            @Valid PageableRequest request) {
        Page<RouteResponse> routes = routeService.getAllRoutes(request);
        return ResponseEntity.ok(BaseResponse.success(routes));
    }

    @GetMapping("/{routeId}")
    @Operation(summary = "Get route by ID")
    public ResponseEntity<BaseResponse<RouteResponse>> getRouteById(@PathVariable UUID routeId) {
        RouteResponse route = routeService.getRouteById(routeId);
        return ResponseEntity.ok(BaseResponse.success(route));
    }

    @GetMapping("/number/{routeNumber}")
    @Operation(summary = "Get route by number")
    public ResponseEntity<BaseResponse<RouteResponse>> getRouteByNumber(
            @PathVariable String routeNumber) {
        RouteResponse route = routeService.getRouteByNumber(routeNumber);
        return ResponseEntity.ok(BaseResponse.success(route));
    }

    @PostMapping("/search")
    @Operation(summary = "Search routes between locations")
    public ResponseEntity<BaseResponse<List<RouteResponse>>> searchRoutes(
            @Valid @RequestBody RouteSearchRequest request) {
        List<RouteResponse> routes = routeService.searchRoutes(request);
        return ResponseEntity.ok(BaseResponse.success(routes));
    }

    @GetMapping("/{routeId}/stops")
    @Operation(summary = "Get route stops")
    public ResponseEntity<BaseResponse<List<RouteStopResponse>>> getRouteStops(
            @PathVariable UUID routeId) {
        List<RouteStopResponse> stops = routeService.getRouteStops(routeId);
        return ResponseEntity.ok(BaseResponse.success(stops));
    }

    @GetMapping("/stops/nearby")
    @Operation(summary = "Find nearby stops")
    public ResponseEntity<BaseResponse<List<RouteStopResponse>>> getNearbyStops(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "1.0") double radiusKm) {
        List<RouteStopResponse> stops = routeService.getNearbyStops(latitude, longitude, radiusKm);
        return ResponseEntity.ok(BaseResponse.success(stops));
    }
}