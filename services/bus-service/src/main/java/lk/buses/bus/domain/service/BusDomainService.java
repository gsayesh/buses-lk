package lk.buses.bus.domain.service;

import lk.buses.bus.domain.entity.Bus;
import lk.buses.bus.domain.entity.BusOperator;
import lk.buses.bus.domain.entity.ServiceCategory;
import lk.buses.bus.domain.repository.BusRepository;
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
public class BusDomainService {

    private final BusRepository busRepository;

    @Transactional
    public Bus registerBus(Bus bus) {
        log.debug("Registering bus: {}", bus.getRegistrationNumber());

        // Validate unique registration number
        if (busRepository.findByRegistrationNumber(bus.getRegistrationNumber()).isPresent()) {
            throw new BusinessException("Bus with registration number already exists", "BUS_EXISTS");
        }

        // Validate registration number format (e.g., ND-1122)
        if (!isValidRegistrationNumber(bus.getRegistrationNumber())) {
            throw new BusinessException("Invalid registration number format", "INVALID_REGISTRATION");
        }

        return busRepository.save(bus);
    }

    @Transactional
    public Bus updateBus(UUID busId, Bus updates) {
        log.debug("Updating bus: {}", busId);

        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus", busId.toString()));

        // Update allowed fields
        if (updates.getMake() != null) {
            bus.setMake(updates.getMake());
        }
        if (updates.getModel() != null) {
            bus.setModel(updates.getModel());
        }
        if (updates.getYearOfManufacture() != null) {
            bus.setYearOfManufacture(updates.getYearOfManufacture());
        }
        if (updates.getSeatingCapacity() != null) {
            bus.setSeatingCapacity(updates.getSeatingCapacity());
        }
        if (updates.getBusPhotoUrl() != null) {
            bus.setBusPhotoUrl(updates.getBusPhotoUrl());
        }

        return busRepository.save(bus);
    }

    @Transactional
    public Bus assignGpsDevice(UUID busId, String gpsDeviceId) {
        log.debug("Assigning GPS device {} to bus: {}", gpsDeviceId, busId);

        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus", busId.toString()));

        bus.setHasGpsDevice(true);
        bus.setGpsDeviceId(gpsDeviceId);

        return busRepository.save(bus);
    }

    @Transactional
    public void deactivateBus(UUID busId) {
        log.debug("Deactivating bus: {}", busId);

        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus", busId.toString()));

        bus.setActive(false);
        busRepository.save(bus);
    }

    public List<Bus> findBusesByOperator(UUID operatorId) {
        return busRepository.findActiveByOperator(operatorId);
    }

    public List<Bus> findGpsEnabledBuses() {
        return busRepository.findGpsEnabledBuses();
    }

    private boolean isValidRegistrationNumber(String registrationNumber) {
        // Sri Lankan format: XX-9999 or XXX-9999
        return registrationNumber.matches("^[A-Z]{2,3}-\\d{4}$");
    }
}
