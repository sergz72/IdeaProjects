package org.parts.parts_backend.repository;

import jakarta.persistence.criteria.Predicate;
import lombok.NonNull;
import org.parts.parts_backend.dto.PartSearchDTO;
import org.parts.parts_backend.entity.Part;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PartSpecification {

    public static Specification<@NonNull Part> searchByCriteria(PartSearchDTO search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by category IDs
            if (search.categoryIds != null && !search.categoryIds.isEmpty()) {
                predicates.add(root.get("categoryId").in(search.categoryIds));
            }

            // Filter by size IDs
            if (search.sizeIds != null && !search.sizeIds.isEmpty()) {
                predicates.add(root.get("sizeId").in(search.sizeIds));
            }

            // Filter by unit IDs
            if (search.unitIds != null && !search.unitIds.isEmpty()) {
                predicates.add(root.get("unitId").in(search.unitIds));
            }

            // Filter by precision ID
            if (search.precisionId != null) {
                predicates.add(criteriaBuilder.equal(root.get("precisionId"), search.precisionId));
            }

            // Filter by name (case-insensitive partial match)
            if (search.name != null && !search.name.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + search.name.toLowerCase() + "%"
                ));
            }

            // Filter by value
            if (search.value != null) {
                predicates.add(criteriaBuilder.equal(root.get("value"), search.value));
            }

            // Filter by comment (case-insensitive partial match)
            if (search.comment != null && !search.comment.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("comment")),
                        "%" + search.comment.toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
