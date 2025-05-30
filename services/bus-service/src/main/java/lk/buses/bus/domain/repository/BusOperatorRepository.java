package lk.buses.bus.domain.repository;

import lk.buses.bus.domain.entity.BusOperator;
import lk.buses.common.core.enums.BusOperatorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusOperatorRepository extends JpaRepository<BusOperator, UUID>,
        JpaSpecificationExecutor<BusOperator> {

    Optional<BusOperator> findByOperatorCode(String operatorCode);

    List<BusOperator> findByOperatorType(BusOperatorType operatorType);

    List<BusOperator> findByIsActiveTrue();
}