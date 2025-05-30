package lk.buses.user.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.buses.common.core.dto.BaseResponse;
import lk.buses.user.application.dto.request.*;
import lk.buses.user.application.dto.response.*;
import lk.buses.user.application.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public ResponseEntity<BaseResponse<UserResponse>> register(
            @Valid @RequestBody UserRegistrationRequest request) {
        UserResponse response = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(response, "Registration successful. Please verify your mobile number."));
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    public ResponseEntity<BaseResponse<AuthenticationResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP")
    public ResponseEntity<BaseResponse<Void>> verifyOtp(
            @Valid @RequestBody OtpVerificationRequest request) {
        authenticationService.verifyOtp(request);
        return ResponseEntity.ok(BaseResponse.success(null, "Mobile number verified successfully"));
    }

    @PostMapping("/resend-otp")
    @Operation(summary = "Resend OTP")
    public ResponseEntity<BaseResponse<OtpResponse>> resendOtp(
            @RequestParam String mobileNumber) {
        OtpResponse response = authenticationService.resendOtp(mobileNumber);
        return ResponseEntity.ok(BaseResponse.success(response));
    }
}