package org.parts.parts_backend.repository;

import lombok.NonNull;
import org.parts.parts_backend.entity.Part;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartsRepository extends JpaRepository<@NonNull Part, @NonNull Integer> {
    List<Part> findAll(Specification<@NonNull Part> search, Sort sort);
}