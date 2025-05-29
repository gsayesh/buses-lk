package lk.buses.user.application.service;

import lk.buses.common.core.enums.UserRole;
import lk.buses.common.core.exception.UnauthorizedException;
import lk.buses.user.application.dto.request.*;
import lk.buses.user.application.dto.response.*;
import lk.buses.user.application.mapper.DriverMapper;
import lk.buses.user.domain.entity.DriverProfile;
import lk.buses.user.domain.entity.User;
import lk.buses.user.domain.repository.UserRepository;
import lk.buses.user.domain.service.DriverDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverApplicationService {

    private final UserRepository userRepository;
    private final DriverDomainService driverDomainService;
    private final DriverMapper driverMapper;

    @Transactional
    public DriverProfileResponse registerAsDriver(DriverRegistrationRequest request) {
        log.info("Registering user as driver");

        UUID userId = getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

        // Update user role to DRIVER
        user.setRole(UserRole.DRIVER);
        userRepository.save(user);

        // Create driver profile
        DriverProfile profile = driverDomainService.createDriverProfile(
                user,
                request.getLicenseNumber(),
                request.getOperatorId()
        );

        log.info("Driver profile created: {}", profile.getId());
        return driverMapper.toResponse(profile);
    }

    @Transactional
    public DriverProfileResponse approveDriver(DriverApprovalRequest request) {
        log.info("Approving driver: {}", request.getDriverProfileId());

        // Verify approver has admin rights
        verifyAdminRole();

        UUID approvedBy = getCurrentUserId();
        DriverProfile profile = driverDomainService.approveDriver(
                request.getDriverProfileId(),
                approvedBy
        );

        return driverMapper.toResponse(profile);
    }

    @Transactional
    public DriverProfileResponse assignBusToDriver(BusAssignmentRequest request) {
        log.info("Assigning bus {} to driver {}", request.getBusId(), request.getDriverProfileId());

        // Verify admin or operator role
        verifyAdminOrOperatorRole();

        DriverProfile profile = driverDomainService.assignBus(
                request.getDriverProfileId(),
                request.getBusId()
        );

        return driverMapper.toResponse(profile);
    }

    @Transactional
    public void unassignBusFromDriver(UUID driverProfileId) {
        log.info("Unassigning bus from driver: {}", driverProfileId);

        verifyAdminOrOperatorRole();
        driverDomainService.unassignBus(driverProfileId);
    }

    public List<DriverProfileResponse> getPendingApprovals() {
        log.debug("Getting pending driver approvals");

        verifyAdminRole();

        List<DriverProfile> pendingProfiles = driverDomainService.getPendingApprovals();
        return pendingProfiles.stream()
                .map(driverMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<DriverProfileResponse> getDriversByOperator(UUID operatorId) {
        log.debug("Getting drivers for operator: {}", operatorId);

        List<DriverProfile> drivers = driverDomainService.getDriversByOperator(operatorId);
        return drivers.stream()
                .map(driverMapper::toResponse)
                .collect(Collectors.toList());
    }

    private UUID getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }
        return (UUID) authentication.getPrincipal();
    }

    private void verifyAdminRole() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new UnauthorizedException("Admin access required");
        }
    }

    private void verifyAdminOrOperatorRole() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAccess = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") ||
                        auth.getAuthority().equals("ROLE_OPERATOR"));

        if (!hasAccess) {
            throw new UnauthorizedException("Admin or Operator access required");
        }
    }
}