package lk.buses.user.application.service;

import lk.buses.common.core.exception.BusinessException;
import lk.buses.common.core.exception.UnauthorizedException;
import lk.buses.common.core.util.StringUtil;
import lk.buses.common.messaging.event.UserRegisteredEvent;
import lk.buses.common.messaging.publisher.EventPublisher;
import lk.buses.common.security.JwtService;
import lk.buses.user.application.dto.request.*;
import lk.buses.user.application.dto.response.*;
import lk.buses.user.application.mapper.UserMapper;
import lk.buses.user.domain.entity.User;
import lk.buses.user.domain.service.OtpService;
import lk.buses.user.domain.service.UserDomainService;
import lk.buses.user.infrastructure.external.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static lk.buses.common.messaging.config.RabbitMQConfig.USER_EXCHANGE;
import static lk.buses.common.messaging.config.RabbitMQConfig.USER_REGISTERED_KEY;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserDomainService userDomainService;
    private final OtpService otpService;
    private final SmsService smsService;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final EventPublisher eventPublisher;

    @Transactional
    public UserResponse register(UserRegistrationRequest request) {
        log.info("Registering new user with mobile: {}", request.getMobileNumber());

        // Sanitize mobile number
        String sanitizedMobile = StringUtil.sanitizeMobileNumber(request.getMobileNumber());
        request.setMobileNumber(sanitizedMobile);

        // Create user
        User user = userMapper.toEntity(request);
        user = userDomainService.createUser(user);

        // Generate and send OTP
        String otp = otpService.generateOtp(sanitizedMobile);
        smsService.sendOtp(sanitizedMobile, otp);

        // Publish user registered event
        UserRegisteredEvent event = UserRegisteredEvent.builder()
                .userId(user.getId())
                .mobileNumber(user.getMobileNumber())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        eventPublisher.publish(USER_EXCHANGE, USER_REGISTERED_KEY, event);

        log.info("User registered successfully: {}", user.getId());
        return userMapper.toResponse(user);
    }

    public AuthenticationResponse login(LoginRequest request) {
        log.debug("User login attempt: {}", request.getMobileNumber());

        // Sanitize mobile number
        String sanitizedMobile = StringUtil.sanitizeMobileNumber(request.getMobileNumber());

        // Find user
        User user = userDomainService.findByMobileNumber(sanitizedMobile);

        // Validate password
        if (!userDomainService.validatePassword(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        // Check if user is active
        if (!user.isActive()) {
            throw new BusinessException("User account is deactivated", "ACCOUNT_DEACTIVATED");
        }

        // Check if user is verified
        if (!user.isVerified()) {
            throw new BusinessException("Please verify your mobile number", "NOT_VERIFIED");
        }

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(
                user.getId(),
                user.getMobileNumber(),
                user.getRole()
        );

        String refreshToken = jwtService.generateRefreshToken(
                user.getId(),
                user.getMobileNumber(),
                user.getRole()
        );

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(3600L) // 1 hour
                .user(userMapper.toResponse(user))
                .build();
    }

    @Transactional
    public void verifyOtp(OtpVerificationRequest request) {
        log.debug("Verifying OTP for mobile: {}", request.getMobileNumber());

        String sanitizedMobile = StringUtil.sanitizeMobileNumber(request.getMobileNumber());

        // Verify OTP
        if (!otpService.verifyOtp(sanitizedMobile, request.getOtp())) {
            throw new BusinessException("Invalid or expired OTP", "INVALID_OTP");
        }

        // Mark user as verified
        User user = userDomainService.findByMobileNumber(sanitizedMobile);
        userDomainService.verifyUser(user.getId());

        log.info("User verified successfully: {}", user.getId());
    }

    public OtpResponse resendOtp(String mobileNumber) {
        log.debug("Resending OTP to: {}", mobileNumber);

        String sanitizedMobile = StringUtil.sanitizeMobileNumber(mobileNumber);

        // Check if user exists
        User user = userDomainService.findByMobileNumber(sanitizedMobile);

        if (user.isVerified()) {
            throw new BusinessException("User already verified", "ALREADY_VERIFIED");
        }

        // Generate and send new OTP
        String otp = otpService.generateOtp(sanitizedMobile);
        smsService.sendOtp(sanitizedMobile, otp);

        return OtpResponse.builder()
                .sent(true)
                .message("OTP sent successfully")
                .expiryMinutes(5)
                .build();
    }
}