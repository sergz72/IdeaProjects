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
import org.parts.parts_backend.dto.PrecisionInsertDTO;
import org.parts.parts_backend.entity.Precision;
import org.parts.parts_backend.service.PrecisionsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/precisions")
@Tag(name = "precisions", description = "Precision management APIs")
class PrecisionsController {
    private final PrecisionsService precisionsService;

    PrecisionsController(PrecisionsService service) {
        precisionsService = service;
    }

    @GetMapping
    @Operation(summary = "Get all precisions", description = "Retrieves a list of all precisions")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of precisions")
    List<Precision> all(
            @Parameter(description = "Search keyword to filter precisions by value")
            @RequestParam(required = false) String value) {
        if (value == null || value.trim().isEmpty())
            return precisionsService.findAll();
        try
        {
            BigDecimal bValue = new BigDecimal(value);
            return precisionsService.findByValue(bValue);
        } catch (NumberFormatException e) {
            return new ArrayList<>();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a precision by ID", description = "Retrieves a specific precision by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the precision",
                    content = @Content(schema = @Schema(implementation = Precision.class))),
            @ApiResponse(responseCode = "404", description = "precision not found", content = @Content)
    })
    ResponseEntity<@NonNull Precision> one(@PathVariable int id) {
        try {
            return ResponseEntity.ok(precisionsService.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Create a new precision", description = "Creates a new precision with the provided value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Precision created successfully",
                    content = @Content(schema = @Schema(implementation = Precision.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    ResponseEntity<@NonNull Precision> create(@RequestBody PrecisionInsertDTO precisionDTO) {
        Precision created = precisionsService.insert(precisionDTO.value);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a precision", description = "Updates an existing precision by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Precision updated successfully",
                    content = @Content(schema = @Schema(implementation = Precision.class))),
            @ApiResponse(responseCode = "404", description = "Precision not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    ResponseEntity<@NonNull Precision> update(@PathVariable int id, @RequestBody PrecisionInsertDTO precisionDTO) {
        try {
            Precision precision = new Precision(id, precisionDTO.value);
            Precision updated = precisionsService.update(precision);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a precision", description = "Deletes a precision by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Precision deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Precision not found")
    })
    ResponseEntity<Void> delete(@PathVariable int id) {
        try {
            precisionsService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
