package org.parts.parts_backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.parts.parts_backend.entity.Precision;
import org.parts.parts_backend.repository.PrecisionsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PrecisionsService {
    private final PrecisionsRepository repository;

    PrecisionsService(PrecisionsRepository repository) {
        this.repository = repository;
    }

    public List<Precision> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "value"));
    }

    public List<Precision> findByValue(BigDecimal value) {
        return repository.findByValue(value);
    }

    public Precision findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Precision not found: " + id));
    }

    @Transactional
    public Precision insert(BigDecimal value) {
        Precision Precision = new Precision(value);
        return repository.save(Precision);
    }

    @Transactional
    public Precision update(Precision Precision) {
        if (!repository.existsById(Precision.getId())) {
            throw new EntityNotFoundException("Precision not found: " + Precision.getId());
        }
        return repository.save(Precision);
    }

    @Transactional
    public void delete(int id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Precision not found: " + id);
        }
        repository.deleteById(id);
    }
}
