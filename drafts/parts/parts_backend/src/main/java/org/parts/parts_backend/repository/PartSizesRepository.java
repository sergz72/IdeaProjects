package org.parts.parts_backend.repository;

import lombok.NonNull;
import org.parts.parts_backend.entity.PartSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartSizesRepository extends JpaRepository<@NonNull PartSize, @NonNull String> {
}