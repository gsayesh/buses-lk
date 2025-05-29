package lk.buses.route.domain.repository;

import lk.buses.route.domain.entity.FareStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FareStructureRepository extends JpaRepository<FareStructure, UUID> {

    Optional<FareStructure> findByIsActiveTrue();

    List<FareStructure> findByEffectiveDateLessThanEqualOrderByEffectiveDateDesc(LocalDate date);

    List<FareStructure> findAllByOrderByEffectiveDateDesc();
}