package lk.buses.route.application.service;

import lk.buses.common.core.exception.UnauthorizedException;
import lk.buses.route.application.dto.request.GovernmentFareUpdateRequest;
import lk.buses.route.application.dto.response.FareStructureResponse;
import lk.buses.route.application.mapper.FareMapper;
import lk.buses.route.domain.entity.FareStructure;
import lk.buses.route.domain.entity.RouteFare;
import lk.buses.route.domain.repository.FareStructureRepository;
import lk.buses.route.domain.repository.RouteFareRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GovernmentFareService {

    private final FareStructureRepository fareStructureRepository;
    private final RouteFareRepository routeFareRepository;
    private final FareMapper fareMapper;

    @Transactional
    public FareStructureResponse updateNationwideFares(GovernmentFareUpdateRequest request) {
        log.info("Processing nationwide fare update: {}% increase, minimum fare: {}",
                request.getPercentageIncrease(), request.getNewMinimumFare());

        // Verify government role
        verifyGovernmentRole();

        UUID updatedBy = getCurrentUserId();

        // Get current active fare structure
        FareStructure currentStructure = fareStructureRepository.findByIsActiveTrue()
                .orElseThrow(() -> new BusinessException("No active fare structure found", "NO_ACTIVE_FARE"));

        // Create new fare structure
        FareStructure newStructure = FareStructure.builder()
                .effectiveDate(request.getEffectiveDate())
                .minimumFare(request.getNewMinimumFare())
                .farePerKm(calculateNewFarePerKm(currentStructure.getFarePerKm(), request.getPercentageIncrease()))
                .createdBy(updatedBy)
                .notes(request.getNotes())
                .isActive(false) // Will be activated on effective date
                .build();

        fareStructureRepository.save(newStructure);

        // Copy and update all route fares
        updateAllRouteFares(currentStructure.getId(), newStructure.getId(),
                request.getPercentageIncrease(), request.getNewMinimumFare());

        // Schedule activation for effective date
        scheduleFareActivation(newStructure.getId(), request.getEffectiveDate());

        log.info("Fare update completed for structure: {}", newStructure.getId());

        return fareMapper.toResponse(newStructure);
    }

    private BigDecimal calculateNewFarePerKm(BigDecimal currentFarePerKm, BigDecimal percentageIncrease) {
        BigDecimal multiplier = BigDecimal.ONE.add(percentageIncrease.divide(BigDecimal.valueOf(100)));
        return currentFarePerKm.multiply(multiplier);
    }

    private void updateAllRouteFares(UUID currentStructureId, UUID newStructureId,
                                     BigDecimal percentageIncrease, BigDecimal newMinimumFare) {
        List<RouteFare> currentFares = routeFareRepository.findByFareStructureId(currentStructureId);

        BigDecimal multiplier = BigDecimal.ONE.add(percentageIncrease.divide(BigDecimal.valueOf(100)));

        List<RouteFare> newFares = currentFares.stream()
                .map(fare -> {
                    BigDecimal newBaseFare = fare.getBaseFare().multiply(multiplier);

                    // Ensure minimum fare
                    if (newBaseFare.compareTo(newMinimumFare) < 0) {
                        newBaseFare = newMinimumFare;
                    }

                    return RouteFare.builder()
                            .route(fare.getRoute())
                            .fareStructure(FareStructure.builder().id(newStructureId).build())
                            .fromStop(fare.getFromStop())
                            .toStop(fare.getToStop())
                            .baseFare(newBaseFare)
                            .build();
                })
                .collect(Collectors.toList());

        routeFareRepository.saveAll(newFares);
    }

    private void scheduleFareActivation(UUID fareStructureId, LocalDate effectiveDate) {
        // TODO: Implement scheduling logic
        // This could use Spring's @Scheduled or a job scheduler like Quartz
        log.info("Scheduled fare activation for {} on {}", fareStructureId, effectiveDate);
    }

    public List<FareStructureResponse> getFareHistory() {
        log.debug("Getting fare structure history");

        List<FareStructure> structures = fareStructureRepository.findAllByOrderByEffectiveDateDesc();
        return structures.stream()
                .map(fareMapper::toResponse)
                .collect(Collectors.toList());
    }

    private void verifyGovernmentRole() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isGovernment = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_GOVERNMENT"));

        if (!isGovernment) {
            throw new UnauthorizedException("Government access required");
        }
    }

    private UUID getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UUID) authentication.getPrincipal();
    }
}