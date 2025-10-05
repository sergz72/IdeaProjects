package org.parts.parts_backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.parts.parts_backend.entity.Category;
import org.parts.parts_backend.repository.CategoriesRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriesService {
    private final CategoriesRepository repository;

    CategoriesService(CategoriesRepository repository) {
        this.repository = repository;
    }

    public List<Category> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public List<Category> findByNameContainingIgnoreCase(String searchString) {
        return repository.findByNameContainingIgnoreCase(searchString, Sort.by(Sort.Direction.ASC, "name"));
    }

    public Category findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + id));
    }

    @Transactional
    public Category insert(String name) {
        Category category = new Category(name);
        return repository.save(category);
    }

    @Transactional
    public Category update(Category category) {
        if (!repository.existsById(category.getId())) {
            throw new EntityNotFoundException("Category not found: " + category.getId());
        }
        return repository.save(category);
    }

    @Transactional
    public void delete(int id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Category not found: " + id);
        }
        repository.deleteById(id);
    }
}
