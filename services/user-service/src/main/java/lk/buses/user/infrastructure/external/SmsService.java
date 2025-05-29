package lk.buses.user.infrastructure.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {

    public void sendOtp(String mobileNumber, String otp) {
        // TODO: Integrate with actual SMS gateway
        log.info("Sending OTP {} to mobile: {}", otp, mobileNumber);

        // Mock implementation - in production, integrate with SMS provider
        // Example: Twilio, Dialog SMS Gateway, etc.
    }

    public void sendSms(String mobileNumber, String message) {
        log.info("Sending SMS to {}: {}", mobileNumber, message);

        // Mock implementation
    }
}