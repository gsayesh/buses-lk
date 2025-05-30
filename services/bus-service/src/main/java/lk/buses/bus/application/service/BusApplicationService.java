package lk.buses.bus.application.service;

import lk.buses.bus.application.dto.request.*;
import lk.buses.bus.application.dto.response.*;
import lk.buses.bus.application.mapper.BusMapper;
import lk.buses.bus.domain.entity.*;
import lk.buses.bus.domain.repository.*;
import lk.buses.bus.domain.service.BusDomainService;
import lk.buses.common.core.dto.PageableRequest;
import lk.buses.common.core.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusApplicationService {

    private final BusRepository busRepository;
    private final BusOperatorRepository operatorRepository;
    private final ServiceCategoryRepository categoryRepository;
    private final BusDomainService busDomainService;
    private final BusMapper busMapper;

    @Transactional
    public BusResponse registerBus(RegisterBusRequest request) {
        log.info("Registering new bus: {}", request.getRegistrationNumber());

        // Get operator
        BusOperator operator = operatorRepository.findById(request.getOperatorId())
                .orElseThrow(() -> new ResourceNotFoundException("Operator", request.getOperatorId().toString()));

        // Get service category
        ServiceCategory category = categoryRepository.findByCategory(request.getServiceCategory())
                .orElseThrow(() -> new ResourceNotFoundException("Service category not found"));

        // Create bus entity
        Bus bus = busMapper.toEntity(request);
        bus.setOperator(operator);
        bus.setServiceCategory(category);

        bus = busDomainService.registerBus(bus);

        log.info("Bus registered successfully: {}", bus.getId());
        return busMapper.toResponse(bus);
    }

    public BusResponse getBusById(UUID busId) {
        log.debug("Getting bus by ID: {}", busId);

        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus", busId.toString()));

        return busMapper.toResponse(bus);
    }

    public BusResponse getBusByRegistrationNumber(String registrationNumber) {
        log.debug("Getting bus by registration number: {}", registrationNumber);

        Bus bus = busRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with registration: " + registrationNumber));

        return busMapper.toResponse(bus);
    }

    public Page<BusResponse> searchBuses(BusSearchRequest searchRequest, PageableRequest pageRequest) {
        log.debug("Searching buses with criteria: {}", searchRequest);

        Specification<Bus> spec = Specification.where(null);

        if (searchRequest.getOperatorId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("operator").get("id"), searchRequest.getOperatorId()));
        }

        if (searchRequest.getServiceCategory() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("serviceCategory").get("category"), searchRequest.getServiceCategory()));
        }

        if (searchRequest.getMinCapacity() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("seatingCapacity"), searchRequest.getMinCapacity()));
        }

        if (searchRequest.getHasGps() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("hasGpsDevice"), searchRequest.getHasGps()));
        }

        if (searchRequest.getIsActive() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("isActive"), searchRequest.getIsActive()));
        }

        PageRequest pageable = PageRequest.of(
                pageRequest.getPage(),
                pageRequest.getSize(),
                Sort.by(Sort.Direction.valueOf(pageRequest.getSortDirection()), pageRequest.getSortBy())
        );

        Page<Bus> buses = busRepository.findAll(spec, pageable);
        return buses.map(busMapper::toResponse);
    }

    @Transactional
    public BusResponse assignGpsDevice(GpsDeviceAssignmentRequest request) {
        log.info("Assigning GPS device {} to bus {}", request.getGpsDeviceId(), request.getBusId());

        Bus bus = busDomainService.assignGpsDevice(request.getBusId(), request.getGpsDeviceId());

        return busMapper.toResponse(bus);
    }

    @Transactional
    public void deactivateBus(UUID busId) {
        log.info("Deactivating bus: {}", busId);

        busDomainService.deactivateBus(busId);
    }
}