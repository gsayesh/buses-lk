package lk.buses.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.buses.common.core.dto.BaseResponse;
import lk.buses.user.application.dto.request.*;
import lk.buses.user.application.dto.response.*;
import lk.buses.user.application.service.DriverApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
@Tag(name = "Drivers", description = "Driver management APIs")
@SecurityRequirement(name = "bearerAuth")
public class DriverController {

    private final DriverApplicationService driverService;

    @PostMapping("/register")
    @Operation(summary = "Register as driver")
    public ResponseEntity<BaseResponse<DriverProfileResponse>> registerAsDriver(
            @Valid @RequestBody DriverRegistrationRequest request) {
        DriverProfileResponse response = driverService.registerAsDriver(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(response, "Driver registration submitted for approval"));
    }

    @PostMapping("/approve")
    @Operation(summary = "Approve driver registration")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<DriverProfileResponse>> approveDriver(
            @Valid @RequestBody DriverApprovalRequest request) {
        DriverProfileResponse response = driverService.approveDriver(request);
        return ResponseEntity.ok(BaseResponse.success(response, "Driver approved successfully"));
    }

    @PostMapping("/assign-bus")
    @Operation(summary = "Assign bus to driver")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<BaseResponse<DriverProfileResponse>> assignBus(
            @Valid @RequestBody BusAssignmentRequest request) {
        DriverProfileResponse response = driverService.assignBusToDriver(request);
        return ResponseEntity.ok(BaseResponse.success(response, "Bus assigned successfully"));
    }

    @DeleteMapping("/{driverProfileId}/bus")
    @Operation(summary = "Unassign bus from driver")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<BaseResponse<Void>> unassignBus(@PathVariable UUID driverProfileId) {
        driverService.unassignBusFromDriver(driverProfileId);
        return ResponseEntity.ok(BaseResponse.success(null, "Bus unassigned successfully"));
    }

    @GetMapping("/pending-approvals")
    @Operation(summary = "Get pending driver approvals")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<List<DriverProfileResponse>>> getPendingApprovals() {
        List<DriverProfileResponse> response = driverService.getPendingApprovals();
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @GetMapping("/operator/{operatorId}")
    @Operation(summary = "Get drivers by operator")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<BaseResponse<List<DriverProfileResponse>>> getDriversByOperator(
            @PathVariable UUID operatorId) {
        List<DriverProfileResponse> response = driverService.getDriversByOperator(operatorId);
        return ResponseEntity.ok(BaseResponse.success(response));
    }
}