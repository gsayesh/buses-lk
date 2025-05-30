package lk.buses.bus.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.buses.bus.application.dto.request.CreateOperatorRequest;
import lk.buses.bus.application.dto.response.BusOperatorResponse;
import lk.buses.bus.application.service.OperatorApplicationService;
import lk.buses.common.core.dto.BaseResponse;
import lk.buses.common.core.dto.PageableRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/operators")
@RequiredArgsConstructor
@Tag(name = "Operators", description = "Bus operator management APIs")
public class OperatorController {

    private final OperatorApplicationService operatorService;

    @GetMapping
    @Operation(summary = "Get all operators")
    public ResponseEntity<BaseResponse<Page<BusOperatorResponse>>> getAllOperators(
            @Valid PageableRequest request) {
        Page<BusOperatorResponse> operators = operatorService.getAllOperators(request);
        return ResponseEntity.ok(BaseResponse.success(operators));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active operators")
    public ResponseEntity<BaseResponse<List<BusOperatorResponse>>> getActiveOperators() {
        List<BusOperatorResponse> operators = operatorService.getActiveOperators();
        return ResponseEntity.ok(BaseResponse.success(operators));
    }

    @GetMapping("/{operatorId}")
    @Operation(summary = "Get operator by ID")
    public ResponseEntity<BaseResponse<BusOperatorResponse>> getOperatorById(
            @PathVariable UUID operatorId) {
        BusOperatorResponse operator = operatorService.getOperatorById(operatorId);
        return ResponseEntity.ok(BaseResponse.success(operator));
    }

    @GetMapping("/code/{operatorCode}")
    @Operation(summary = "Get operator by code")
    public ResponseEntity<BaseResponse<BusOperatorResponse>> getOperatorByCode(
            @PathVariable String operatorCode) {
        BusOperatorResponse operator = operatorService.getOperatorByCode(operatorCode);
        return ResponseEntity.ok(BaseResponse.success(operator));
    }

    @PostMapping
    @Operation(summary = "Create new operator")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BaseResponse<BusOperatorResponse>> createOperator(
            @Valid @RequestBody CreateOperatorRequest request) {
        BusOperatorResponse operator = operatorService.createOperator(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(operator, "Operator created successfully"));
    }

    @DeleteMapping("/{operatorId}")
    @Operation(summary = "Deactivate operator")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BaseResponse<Void>> deactivateOperator(@PathVariable UUID operatorId) {
        operatorService.deactivateOperator(operatorId);
        return ResponseEntity.ok(BaseResponse.success(null, "Operator deactivated successfully"));
    }
}