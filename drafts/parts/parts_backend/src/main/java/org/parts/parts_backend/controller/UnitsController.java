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
import org.parts.parts_backend.entity.Unit;
import org.parts.parts_backend.service.UnitsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/units")
@Tag(name = "Units", description = "Units management APIs")
class UnitsController {
    private final UnitsService service;

    UnitsController(UnitsService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all units", description = "Retrieves a list of all units")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of units")
    List<Unit> all(
            @Parameter(description = "Search keyword to filter units by id (case-insensitive)")
            @RequestParam(required = false) String id) {
        var trimmed = id == null ? null : id.trim();
        if (trimmed == null || trimmed.isEmpty())
            return service.findAll();
        return service.findByIdContainingIgnoreCase(trimmed);
    }

    @PostMapping
    @Operation(summary = "Create a new unit", description = "Creates a new unit with the provided id and multiplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Unit created successfully",
                    content = @Content(schema = @Schema(implementation = Unit.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    ResponseEntity<@NonNull Unit> create(@RequestBody Unit unit) {
        service.insert(unit);
        return ResponseEntity.status(HttpStatus.CREATED).body(unit);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a unit", description = "Updates an existing unit by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unit updated successfully",
                    content = @Content(schema = @Schema(implementation = Unit.class))),
            @ApiResponse(responseCode = "404", description = "Unit not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    ResponseEntity<@NonNull Unit> update(@PathVariable String id, @RequestBody Unit unit) {
        try {
            service.update(id, unit);
            return ResponseEntity.ok(unit);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a unit", description = "Deletes a unit by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Unit deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Unit not found")
    })
    ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
