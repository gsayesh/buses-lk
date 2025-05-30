package lk.buses.route.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.buses.common.core.dto.BaseResponse;
import lk.buses.route.application.dto.request.FareCalculationRequest;
import lk.buses.route.application.dto.response.*;
import lk.buses.route.application.service.FareApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fares")
@RequiredArgsConstructor
@Tag(name = "Fares", description = "Fare calculation APIs")
public class FareController {

    private final FareApplicationService fareService;

    @PostMapping("/calculate")
    @Operation(summary = "Calculate fare for a journey")
    public ResponseEntity<BaseResponse<FareCalculationResponse>> calculateFare(
            @Valid @RequestBody FareCalculationRequest request) {
        FareCalculationResponse fare = fareService.calculateFare(request);
        return ResponseEntity.ok(BaseResponse.success(fare));
    }

    @GetMapping("/breakdown")
    @Operation(summary = "Get fare breakdown for all service categories")
    public ResponseEntity<BaseResponse<FareBreakdownResponse>> getFareBreakdown(
            @RequestParam UUID routeId,
            @RequestParam UUID fromStopId,
            @RequestParam UUID toStopId) {
        FareBreakdownResponse breakdown = fareService.getFareBreakdown(routeId, fromStopId, toStopId);
        return ResponseEntity.ok(BaseResponse.success(breakdown));
    }
}