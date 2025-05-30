package lk.buses.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.buses.common.core.dto.BaseResponse;
import lk.buses.common.core.dto.PageableRequest;
import lk.buses.user.application.dto.request.*;
import lk.buses.user.application.dto.response.*;
import lk.buses.user.application.service.UserApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserApplicationService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<BaseResponse<UserResponse>> getCurrentUser() {
        UserResponse response = userService.getCurrentUser();
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal")
    public ResponseEntity<BaseResponse<UserResponse>> getUserById(@PathVariable UUID userId) {
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user profile")
    @PreAuthorize("#userId == authentication.principal")
    public ResponseEntity<BaseResponse<UserResponse>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(userId, request);
        return ResponseEntity.ok(BaseResponse.success(response, "Profile updated successfully"));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password")
    public ResponseEntity<BaseResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok(BaseResponse.success(null, "Password changed successfully"));
    }

    @PostMapping("/deactivate")
    @Operation(summary = "Deactivate account")
    public ResponseEntity<BaseResponse<Void>> deactivateAccount() {
        userService.deactivateAccount();
        return ResponseEntity.ok(BaseResponse.success(null, "Account deactivated successfully"));
    }

    @GetMapping
    @Operation(summary = "Search users (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<Page<UserResponse>>> searchUsers(
            @Valid PageableRequest request) {
        Page<UserResponse> response = userService.searchUsers(request);
        return ResponseEntity.ok(BaseResponse.success(response));
    }
}
