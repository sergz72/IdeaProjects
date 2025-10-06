package org.parts.parts_backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.parts.parts_backend.dto.PartSearchDTO;
import org.parts.parts_backend.entity.Part;
import org.parts.parts_backend.repository.PartSpecification;
import org.parts.parts_backend.repository.PartsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PartsService {
    private final PartsRepository repository;

    PartsService(PartsRepository repository) {
        this.repository = repository;
    }

    public List<Part> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public List<Part> find(PartSearchDTO search) {
        return repository.findAll(
                PartSpecification.searchByCriteria(search),
                Sort.by(Sort.Direction.ASC, "name")
        );
    }

    public Part findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Part not found: " + id));
    }

    @Transactional
    public Part insert(Part part) {
        return repository.save(part);
    }

    @Transactional
    public Part update(Part newPart) {
        if (!repository.existsById(newPart.getId())) {
            throw new EntityNotFoundException("Part not found: " + newPart.getId());
        }
        return repository.save(newPart);
    }

    @Transactional
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Part not found: " + id);
        }
        repository.deleteById(id);
    }
}
