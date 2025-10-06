package org.parts.parts_backend.repository;

import lombok.NonNull;
import org.parts.parts_backend.entity.Unit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UnitsRepository extends JpaRepository<@NonNull Unit, @NonNull String> {
    List<Unit> findByIdContainingIgnoreCase(String keyword, Sort sort);
    List<Unit> findByMultiplier(BigDecimal value);
}