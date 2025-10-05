package org.parts.parts_backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.parts.parts_backend.entity.PartSize;
import org.parts.parts_backend.repository.PartSizesRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PartSizesService {
    private final PartSizesRepository repository;

    PartSizesService(PartSizesRepository repository) {
        this.repository = repository;
    }

    public List<PartSize> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public PartSize findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Part size not found: " + id));
    }

    @Transactional
    public void insert(PartSize size) {
        repository.save(size);
    }

    @Transactional
    public void update(String id, String newId) {
        delete(id);
        repository.save(new PartSize(newId));
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Part size not found: " + id);
        }
        repository.deleteById(id);
    }
}
