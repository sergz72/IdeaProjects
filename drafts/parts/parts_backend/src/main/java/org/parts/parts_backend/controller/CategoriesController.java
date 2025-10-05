package org.parts.parts_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.parts.parts_backend.dto.CategoryInsertDTO;
import org.parts.parts_backend.entity.Category;
import org.parts.parts_backend.service.CategoriesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Category management APIs")
class CategoriesController {
    private final CategoriesService categoriesService;

    CategoriesController(CategoriesService service) {
        categoriesService = service;
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieves a list of all categories")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of categories")
    List<Category> all(
            @Parameter(description = "Search keyword to filter categories by name (case-insensitive)")
            @RequestParam(required = false) String name) {
        var trimmed = name == null ? null : name.trim();
        if (trimmed == null || trimmed.isEmpty())
            return categoriesService.findAll();
        return categoriesService.findByNameContainingIgnoreCase(trimmed);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a category by ID", description = "Retrieves a specific category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the category",
                    content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    ResponseEntity<@NonNull Category> one(@PathVariable int id) {
        try {
            return ResponseEntity.ok(categoriesService.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Create a new category", description = "Creates a new category with the provided name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    ResponseEntity<@NonNull Category> create(@RequestBody CategoryInsertDTO categoryDTO) {
        Category created = categoriesService.insert(categoryDTO.name);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category", description = "Updates an existing category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(schema = @Schema(implementation = Category.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    ResponseEntity<@NonNull Category> update(@PathVariable int id, @RequestBody CategoryInsertDTO categoryDTO) {
        try {
            Category category = new Category(id, categoryDTO.name);
            Category updated = categoriesService.update(category);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category", description = "Deletes a category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    ResponseEntity<Void> delete(@PathVariable int id) {
        try {
            categoriesService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
