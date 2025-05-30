package lk.buses.route.domain.service;

import lk.buses.common.core.enums.ServiceCategory;
import lk.buses.common.core.exception.BusinessException;
import lk.buses.route.domain.entity.FareStructure;
import lk.buses.route.domain.entity.RouteFare;
import lk.buses.route.domain.repository.FareStructureRepository;
import lk.buses.route.domain.repository.RouteFareRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FareCalculationService {

    private final RouteFareRepository routeFareRepository;
    private final FareStructureRepository fareStructureRepository;

    @Cacheable(value = "fareCalculations", key = "#routeId + ':' + #fromStopId + ':' + #toStopId + ':' + #serviceCategory")
    @Transactional(readOnly = true)
    public FareCalculationResult calculateFare(UUID routeId, UUID fromStopId, UUID toStopId,
                                               ServiceCategory serviceCategory) {
        log.debug("Calculating fare for route: {}, from: {}, to: {}, service: {}",
                routeId, fromStopId, toStopId, serviceCategory);

        // Get active fare structure
        FareStructure activeFareStructure = fareStructureRepository.findByIsActiveTrue()
                .orElseThrow(() -> new BusinessException("No active fare structure found", "NO_FARE_STRUCTURE"));

        // Get base fare for the segment
        RouteFare routeFare = routeFareRepository
                .findByRouteIdAndFromStopIdAndToStopIdAndFareStructureId(
                        routeId, fromStopId, toStopId, activeFareStructure.getId()
                )
                .orElseThrow(() -> new BusinessException("Fare not found for selected route", "FARE_NOT_FOUND"));

        // Calculate final fare with service category multiplier
        BigDecimal baseFare = routeFare.getBaseFare();
        BigDecimal multiplier = BigDecimal.valueOf(serviceCategory.getFareMultiplier());
        BigDecimal finalFare = baseFare.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);

        // Ensure minimum fare
        if (finalFare.compareTo(activeFareStructure.getMinimumFare()) < 0) {
            finalFare = activeFareStructure.getMinimumFare();
        }

        return FareCalculationResult.builder()
                .routeId(routeId)
                .fromStopId(fromStopId)
                .toStopId(toStopId)
                .serviceCategory(serviceCategory)
                .baseFare(baseFare)
                .serviceMultiplier(multiplier)
                .finalFare(finalFare)
                .fareStructureId(activeFareStructure.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public FareBreakdown calculateFareBreakdown(UUID routeId, UUID fromStopId, UUID toStopId) {
        log.debug("Calculating fare breakdown for all service categories");

        FareBreakdown breakdown = new FareBreakdown();

        for (ServiceCategory category : ServiceCategory.values()) {
            FareCalculationResult result = calculateFare(routeId, fromStopId, toStopId, category);
            breakdown.addFare(category, result.getFinalFare());
        }

        return breakdown;
    }

    @lombok.Data
    @lombok.Builder
    public static class FareCalculationResult {
        private UUID routeId;
        private UUID fromStopId;
        private UUID toStopId;
        private ServiceCategory serviceCategory;
        private BigDecimal baseFare;
        private BigDecimal serviceMultiplier;
        private BigDecimal finalFare;
        private UUID fareStructureId;
    }

    @lombok.Data
    public static class FareBreakdown {
        private BigDecimal normalFare;
        private BigDecimal semiLuxuryFare;
        private BigDecimal acLuxuryFare;
        private BigDecimal superLuxuryFare;

        public void addFare(ServiceCategory category, BigDecimal fare) {
            switch (category) {
                case NORMAL:
                    this.normalFare = fare;
                    break;
                case SEMI_LUXURY:
                    this.semiLuxuryFare = fare;
                    break;
                case AC_LUXURY:
                    this.acLuxuryFare = fare;
                    break;
                case SUPER_LUXURY:
                    this.superLuxuryFare = fare;
                    break;
            }
        }
    }
}