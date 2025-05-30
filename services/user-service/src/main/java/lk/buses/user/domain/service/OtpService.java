package lk.buses.user.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtpService {

    private static final String OTP_PREFIX = "otp:";
    private static final int OTP_LENGTH = 6;
    private static final long OTP_VALIDITY_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 3;

    private final RedisTemplate<String, String> redisTemplate;
    private final SecureRandom secureRandom = new SecureRandom();

    public String generateOtp(String mobileNumber) {
        log.debug("Generating OTP for mobile: {}", mobileNumber);

        String otp = generateRandomOtp();
        String key = OTP_PREFIX + mobileNumber;

        // Store OTP with expiry
        redisTemplate.opsForValue().set(key, otp, Duration.ofMinutes(OTP_VALIDITY_MINUTES));

        // Reset attempts counter
        String attemptsKey = key + ":attempts";
        redisTemplate.delete(attemptsKey);

        return otp;
    }

    public boolean verifyOtp(String mobileNumber, String otp) {
        log.debug("Verifying OTP for mobile: {}", mobileNumber);

        String key = OTP_PREFIX + mobileNumber;
        String attemptsKey = key + ":attempts";

        // Check attempts
        String attempts = redisTemplate.opsForValue().get(attemptsKey);
        int attemptCount = attempts != null ? Integer.parseInt(attempts) : 0;

        if (attemptCount >= MAX_ATTEMPTS) {
            log.warn("Max OTP attempts exceeded for mobile: {}", mobileNumber);
            throw new BusinessException("Maximum OTP attempts exceeded", "MAX_ATTEMPTS_EXCEEDED");
        }

        // Verify OTP
        String storedOtp = redisTemplate.opsForValue().get(key);
        if (storedOtp == null) {
            log.warn("OTP expired or not found for mobile: {}", mobileNumber);
            return false;
        }

        if (!storedOtp.equals(otp)) {
            // Increment attempts
            redisTemplate.opsForValue().increment(attemptsKey);
            redisTemplate.expire(attemptsKey, OTP_VALIDITY_MINUTES, TimeUnit.MINUTES);
            return false;
        }

        // OTP verified, clean up
        redisTemplate.delete(key);
        redisTemplate.delete(attemptsKey);

        return true;
    }

    private String generateRandomOtp() {
        int otp = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
    }
}