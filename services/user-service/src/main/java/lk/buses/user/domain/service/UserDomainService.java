package lk.buses.user.domain.service;

import lk.buses.common.core.exception.BusinessException;
import lk.buses.common.core.exception.ResourceNotFoundException;
import lk.buses.user.domain.entity.User;
import lk.buses.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(User user) {
        log.debug("Creating user with mobile number: {}", user.getMobileNumber());

        // Validate unique constraints
        if (userRepository.existsByMobileNumber(user.getMobileNumber())) {
            throw new BusinessException("Mobile number already registered", "USER_EXISTS");
        }

        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new BusinessException("Email already registered", "EMAIL_EXISTS");
        }

        // Hash password
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UUID userId, User updates) {
        log.debug("Updating user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

        // Update allowed fields
        if (updates.getFirstName() != null) {
            user.setFirstName(updates.getFirstName());
        }
        if (updates.getLastName() != null) {
            user.setLastName(updates.getLastName());
        }
        if (updates.getEmail() != null && !updates.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updates.getEmail())) {
                throw new BusinessException("Email already in use", "EMAIL_EXISTS");
            }
            user.setEmail(updates.getEmail());
        }
        if (updates.getPreferredLanguage() != null) {
            user.setPreferredLanguage(updates.getPreferredLanguage());
        }

        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(UUID userId, String oldPassword, String newPassword) {
        log.debug("Changing password for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new BusinessException("Invalid old password", "INVALID_PASSWORD");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void verifyUser(UUID userId) {
        log.debug("Verifying user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

        user.setVerified(true);
        userRepository.save(user);
    }

    @Transactional
    public void deactivateUser(UUID userId) {
        log.debug("Deactivating user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

        user.setActive(false);
        userRepository.save(user);
    }

    public User findByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with mobile number: " + mobileNumber));
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}