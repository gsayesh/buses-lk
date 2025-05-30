package lk.buses.route.application.service;

import lk.buses.common.core.enums.ServiceCategory;
import lk.buses.route.application.dto.request.*;
import lk.buses.route.application.dto.response.*;
import lk.buses.route.domain.entity.RouteStop;
import lk.buses.route.domain.repository.RouteRepository;
import lk.buses.route.domain.repository.RouteStopRepository;
import lk.buses.route.domain.service.FareCalculationService;
import lk.buses.route.domain.service.FareCalculationService.FareCalculationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FareApplicationService {

    private final FareCalculationService fareCalculationService;
    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;

    public FareCalculationResponse calculateFare(FareCalculationRequest request) {
        log.debug("Calculating fare for request: {}", request);

        FareCalculationResult result = fareCalculationService.calculateFare(
                request.getRouteId(),
                request.getFromStopId(),
                request.getToStopId(),
                request.getServiceCategory()
        );

        // Get route and stop details
        var route = routeRepository.findById(request.getRouteId()).orElseThrow();
        var fromStop = routeStopRepository.findById(request.getFromStopId()).orElseThrow();
        var toStop = routeStopRepository.findById(request.getToStopId()).orElseThrow();

        return FareCalculationResponse.builder()
                .routeId(route.getId())
                .routeNumber(route.getRouteNumber())
                .fromStop(fromStop.getStopNameEn())
                .toStop(toStop.getStopNameEn())
                .serviceCategory(request.getServiceCategory())
                .baseFare(result.getBaseFare())
                .finalFare(result.getFinalFare())
                .minimumFare(result.getFinalFare()) // Already includes minimum fare check
                .build();
    }

    public FareBreakdownResponse getFareBreakdown(UUID routeId, UUID fromStopId, UUID toStopId) {
        log.debug("Getting fare breakdown for route: {}", routeId);

        var breakdown = fareCalculationService.calculateFareBreakdown(routeId, fromStopId, toStopId);

        // Get route and stop details
        var route = routeRepository.findById(routeId).orElseThrow();
        var fromStop = routeStopRepository.findById(fromStopId).orElseThrow();
        var toStop = routeStopRepository.findById(toStopId).orElseThrow();

        return FareBreakdownResponse.builder()
                .routeId(route.getId())
                .routeNumber(route.getRouteNumber())
                .fromStop(fromStop.getStopNameEn())
                .toStop(toStop.getStopNameEn())
                .normalFare(breakdown.getNormalFare())
                .semiLuxuryFare(breakdown.getSemiLuxuryFare())
                .acLuxuryFare(breakdown.getAcLuxuryFare())
                .superLuxuryFare(breakdown.getSuperLuxuryFare())
                .build();
    }
}
