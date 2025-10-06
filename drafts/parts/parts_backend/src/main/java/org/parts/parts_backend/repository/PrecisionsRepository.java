package org.parts.parts_backend.repository;

import lombok.NonNull;
import org.parts.parts_backend.entity.Precision;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PrecisionsRepository extends JpaRepository<@NonNull Precision, @NonNull Integer> {
    List<Precision> findByValue(BigDecimal value);
}
