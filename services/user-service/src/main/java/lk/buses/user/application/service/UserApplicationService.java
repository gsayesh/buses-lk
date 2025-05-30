package lk.buses.user.application.service;

import lk.buses.common.core.dto.PageableRequest;
import lk.buses.common.core.exception.ResourceNotFoundException;
import lk.buses.common.core.exception.UnauthorizedException;
import lk.buses.user.application.dto.request.*;
import lk.buses.user.application.dto.response.*;
import lk.buses.user.application.mapper.UserMapper;
import lk.buses.user.domain.entity.User;
import lk.buses.user.domain.repository.UserRepository;
import lk.buses.user.domain.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final UserMapper userMapper;

    public UserResponse getUserById(UUID userId) {
        log.debug("Getting user by ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.toString()));

        return userMapper.toResponse(user);
    }

    public UserResponse getCurrentUser() {
        UUID userId = getCurrentUserId();
        return getUserById(userId);
    }

    @Transactional
    public UserResponse updateUser(UUID userId, UserUpdateRequest request) {
        log.debug("Updating user: {}", userId);

        // Verify current user can update this profile
        UUID currentUserId = getCurrentUserId();
        if (!currentUserId.equals(userId)) {
            throw new UnauthorizedException("Cannot update other user's profile");
        }

        User updates = new User();
        updates.setFirstName(request.getFirstName());
        updates.setLastName(request.getLastName());
        updates.setEmail(request.getEmail());
        updates.setPreferredLanguage(request.getPreferredLanguage());

        User updatedUser = userDomainService.updateUser(userId, updates);

        return userMapper.toResponse(updatedUser);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        log.debug("Changing password for current user");

        UUID userId = getCurrentUserId();
        userDomainService.changePassword(userId, request.getOldPassword(), request.getNewPassword());
    }

    @Transactional
    public void deactivateAccount() {
        log.debug("Deactivating current user account");

        UUID userId = getCurrentUserId();
        userDomainService.deactivateUser(userId);

        // Clear security context
        SecurityContextHolder.clearContext();
    }

    public Page<UserResponse> searchUsers(PageableRequest request) {
        log.debug("Searching users with page: {}, size: {}", request.getPage(), request.getSize());

        PageRequest pageRequest = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Sort.Direction.valueOf(request.getSortDirection()), request.getSortBy())
        );

        Page<User> users = userRepository.findAll(pageRequest);

        return users.map(userMapper::toResponse);
    }

    private UUID getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }
        return (UUID) authentication.getPrincipal();
    }
}