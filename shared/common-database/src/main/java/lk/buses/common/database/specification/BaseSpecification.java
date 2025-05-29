package lk.buses.common.database.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BaseSpecification {

    public static <T> Specification<T> hasFieldEqual(String fieldName, Object value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(fieldName), value);
        };
    }

    public static <T> Specification<T> hasFieldLike(String fieldName, String value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null || value.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(fieldName)),
                    "%" + value.toLowerCase() + "%"
            );
        };
    }

    public static <T> Specification<T> isActive() {
        return hasFieldEqual("isActive", true);
    }

    public static <T> Specification<T> betweenDates(String fieldName,
                                                    LocalDate startDate,
                                                    LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get(fieldName), startDate));
            }

            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get(fieldName), endDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}