package org.parts.parts_backend.repository;

import lombok.NonNull;
import org.parts.parts_backend.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<@NonNull Category, @NonNull Integer> {
    List<Category> findByNameContainingIgnoreCase(String keyword, Sort sort);
}