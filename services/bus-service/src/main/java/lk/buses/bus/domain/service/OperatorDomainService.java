package lk.buses.bus.domain.service;

import lk.buses.bus.domain.entity.BusOperator;
import lk.buses.bus.domain.repository.BusOperatorRepository;
import lk.buses.common.core.exception.BusinessException;
import lk.buses.common.core.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OperatorDomainService {

    private final BusOperatorRepository operatorRepository;

    @Transactional
    public BusOperator createOperator(BusOperator operator) {
        log.debug("Creating operator: {}", operator.getOperatorCode());

        // Validate unique operator code
        if (operatorRepository.findByOperatorCode(operator.getOperatorCode()).isPresent()) {
            throw new BusinessException("Operator code already exists", "OPERATOR_EXISTS");
        }

        return operatorRepository.save(operator);
    }

    @Transactional
    public BusOperator updateOperator(UUID operatorId, BusOperator updates) {
        log.debug("Updating operator: {}", operatorId);

        BusOperator operator = operatorRepository.findById(operatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Operator", operatorId.toString()));

        // Update allowed fields
        if (updates.getOperatorName() != null) {
            operator.setOperatorName(updates.getOperatorName());
        }
        if (updates.getContactNumber() != null) {
            operator.setContactNumber(updates.getContactNumber());
        }
        if (updates.getEmail() != null) {
            operator.setEmail(updates.getEmail());
        }
        if (updates.getAddress() != null) {
            operator.setAddress(updates.getAddress());
        }

        return operatorRepository.save(operator);
    }

    @Transactional
    public void deactivateOperator(UUID operatorId) {
        log.debug("Deactivating operator: {}", operatorId);

        BusOperator operator = operatorRepository.findById(operatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Operator", operatorId.toString()));

        operator.setActive(false);
        operatorRepository.save(operator);
    }

    public List<BusOperator> getActiveOperators() {
        return operatorRepository.findByIsActiveTrue();
    }
}