package lk.buses.bus.application.service;

import lk.buses.bus.application.dto.request.CreateOperatorRequest;
import lk.buses.bus.application.dto.response.BusOperatorResponse;
import lk.buses.bus.application.mapper.OperatorMapper;
import lk.buses.bus.domain.entity.BusOperator;
import lk.buses.bus.domain.repository.BusOperatorRepository;
import lk.buses.bus.domain.service.OperatorDomainService;
import lk.buses.common.core.dto.PageableRequest;
import lk.buses.common.core.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OperatorApplicationService {

    private final BusOperatorRepository operatorRepository;
    private final OperatorDomainService operatorDomainService;
    private final OperatorMapper operatorMapper;

    @Transactional
    public BusOperatorResponse createOperator(CreateOperatorRequest request) {
        log.info("Creating new operator: {}", request.getOperatorCode());

        BusOperator operator = operatorMapper.toEntity(request);
        operator = operatorDomainService.createOperator(operator);

        log.info("Operator created successfully: {}", operator.getId());
        return operatorMapper.toResponse(operator);
    }

    public BusOperatorResponse getOperatorById(UUID operatorId) {
        log.debug("Getting operator by ID: {}", operatorId);

        BusOperator operator = operatorRepository.findById(operatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Operator", operatorId.toString()));

        return operatorMapper.toResponse(operator);
    }

    public BusOperatorResponse getOperatorByCode(String operatorCode) {
        log.debug("Getting operator by code: {}", operatorCode);

        BusOperator operator = operatorRepository.findByOperatorCode(operatorCode)
                .orElseThrow(() -> new ResourceNotFoundException("Operator not found with code: " + operatorCode));

        return operatorMapper.toResponse(operator);
    }

    public List<BusOperatorResponse> getActiveOperators() {
        log.debug("Getting all active operators");

        List<BusOperator> operators = operatorDomainService.getActiveOperators();
        return operatorMapper.toResponseList(operators);
    }

    public Page<BusOperatorResponse> getAllOperators(PageableRequest request) {
        log.debug("Getting all operators - page: {}, size: {}", request.getPage(), request.getSize());

        PageRequest pageRequest = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Sort.Direction.valueOf(request.getSortDirection()), request.getSortBy())
        );

        Page<BusOperator> operators = operatorRepository.findAll(pageRequest);
        return operators.map(operatorMapper::toResponse);
    }

    @Transactional
    public void deactivateOperator(UUID operatorId) {
        log.info("Deactivating operator: {}", operatorId);

        operatorDomainService.deactivateOperator(operatorId);
    }
}