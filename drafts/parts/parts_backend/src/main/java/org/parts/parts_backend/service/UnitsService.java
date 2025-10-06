package org.parts.parts_backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.parts.parts_backend.entity.Unit;
import org.parts.parts_backend.repository.UnitsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UnitsService {
    private final UnitsRepository repository;

    UnitsService(UnitsRepository repository) {
        this.repository = repository;
    }

    public List<Unit> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<Unit> findByIdContainingIgnoreCase(String searchString) {
        return repository.findByIdContainingIgnoreCase(searchString, Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<Unit> findByMultiplier(BigDecimal value) {
        return repository.findByMultiplier(value);
    }

    public Unit findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unit not found: " + id));
    }

    @Transactional
    public void insert(Unit unit) {
        repository.save(unit);
    }

    @Transactional
    public void update(String id, Unit newUnit) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Unit not found: " + id);
        }
        if (!id.equals(newUnit.getId())) {
            delete(id);
        }
        repository.save(newUnit);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Unit not found: " + id);
        }
        repository.deleteById(id);
    }
}
