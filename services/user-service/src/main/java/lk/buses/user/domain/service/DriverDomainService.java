package lk.buses.user.domain.service;

import lk.buses.common.core.exception.BusinessException;
import lk.buses.common.core.exception.ResourceNotFoundException;
import lk.buses.user.domain.entity.DriverProfile;
import lk.buses.user.domain.entity.User;
import lk.buses.user.domain.repository.DriverProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverDomainService {

    private final DriverProfileRepository driverProfileRepository;

    @Transactional
    public DriverProfile createDriverProfile(User user, String licenseNumber, UUID operatorId) {
        log.debug("Creating driver profile for user: {}", user.getId());

        // Check if driver profile already exists
        if (driverProfileRepository.findByUserId(user.getId()).isPresent()) {
            throw new BusinessException("Driver profile already exists", "PROFILE_EXISTS");
        }

        // Check license number uniqueness
        if (driverProfileRepository.findByLicenseNumber(licenseNumber).isPresent()) {
            throw new BusinessException("License number already registered", "LICENSE_EXISTS");
        }

        DriverProfile profile = DriverProfile.builder()
                .user(user)
                .licenseNumber(licenseNumber)
                .operatorId(operatorId)
                .isApproved(false)
                .build();

        return driverProfileRepository.save(profile);
    }

    @Transactional
    public DriverProfile approveDriver(UUID profileId, UUID approvedBy) {
        log.debug("Approving driver profile: {}", profileId);

        DriverProfile profile = driverProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver profile", profileId.toString()));

        if (profile.isApproved()) {
            throw new BusinessException("Driver already approved", "ALREADY_APPROVED");
        }

        profile.setApproved(true);
        profile.setApprovedBy(approvedBy);
        profile.setApprovedAt(Instant.now());

        return driverProfileRepository.save(profile);
    }

    @Transactional
    public DriverProfile assignBus(UUID profileId, UUID busId) {
        log.debug("Assigning bus {} to driver profile: {}", busId, profileId);

        DriverProfile profile = driverProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver profile", profileId.toString()));

        if (!profile.isApproved()) {
            throw new BusinessException("Cannot assign bus to unapproved driver", "DRIVER_NOT_APPROVED");
        }

        // Check if bus is already assigned to another driver
        List<DriverProfile> driversWithBus = driverProfileRepository.findByAssignedBusId(busId);
        if (!driversWithBus.isEmpty() && !driversWithBus.get(0).getId().equals(profileId)) {
            throw new BusinessException("Bus already assigned to another driver", "BUS_ALREADY_ASSIGNED");
        }

        profile.setAssignedBusId(busId);
        return driverProfileRepository.save(profile);
    }

    @Transactional
    public void unassignBus(UUID profileId) {
        log.debug("Unassigning bus from driver profile: {}", profileId);

        DriverProfile profile = driverProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver profile", profileId.toString()));

        profile.setAssignedBusId(null);
        driverProfileRepository.save(profile);
    }

    public List<DriverProfile> getPendingApprovals() {
        return driverProfileRepository.findPendingApprovals();
    }

    public List<DriverProfile> getDriversByOperator(UUID operatorId) {
        return driverProfileRepository.findApprovedDriversByOperator(operatorId);
    }
}