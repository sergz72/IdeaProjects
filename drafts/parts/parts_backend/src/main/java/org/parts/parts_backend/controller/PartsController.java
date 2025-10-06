package org.parts.parts_backend.controller;

import lombok.NonNull;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.parts.parts_backend.dto.PartSearchDTO;
import org.parts.parts_backend.entity.Part;
import org.parts.parts_backend.service.PartsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/parts")
@Tag(name = "Parts", description = "Parts management APIs")
class PartsController {
    private final PartsService partsService;

    PartsController(PartsService service) {
        partsService = service;
    }

    @GetMapping
    @Operation(summary = "Get parts", description = "Retrieves a list of parts")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of parts")
    List<Part> all(
            @Parameter(description = "Search keyword to filter parts by name")
            @RequestParam(required = false) String name,
            @Parameter(description = "Category IDs to filter parts")
            @RequestParam(required = false) Integer[] categoryIds,
            @Parameter(description = "Size IDs to filter parts")
            @RequestParam(required = false) String[] sizeIds,
            @Parameter(description = "Unit IDs to filter parts")
            @RequestParam(required = false) String[] unitIds,
            @Parameter(description = "Precision ID to filter parts")
            @RequestParam(required = false) Integer precisionId,
            @Parameter(description = "Value value to filter parts")
            @RequestParam(required = false) BigDecimal value,
            @Parameter(description = "Comment value to filter parts")
            @RequestParam(required = false) String comment
            ) {
        PartSearchDTO search = new PartSearchDTO(
                categoryIds == null ? List.of() : Arrays.stream(categoryIds).toList(),
                sizeIds == null ? List.of() : Arrays.stream(sizeIds).toList(),
                unitIds == null ? List.of() : Arrays.stream(unitIds).toList(),
                precisionId, name, value, comment);
        return partsService.find(search);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a part by ID", description = "Retrieves a specific part by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the part",
                    content = @Content(schema = @Schema(implementation = Part.class))),
            @ApiResponse(responseCode = "404", description = "Part not found", content = @Content)
    })
    ResponseEntity<@NonNull Part> one(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(partsService.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Create a new part", description = "Creates a new part with the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Part created successfully",
                    content = @Content(schema = @Schema(implementation = Part.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    ResponseEntity<@NonNull Part> create(@RequestBody Part part) {
        Part created = partsService.insert(part);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a part", description = "Updates an existing part by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Part updated successfully",
                    content = @Content(schema = @Schema(implementation = Part.class))),
            @ApiResponse(responseCode = "404", description = "Part not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    ResponseEntity<@NonNull Part> update(@PathVariable Integer id, @RequestBody Part part) {
        try {
            part.setId(id);
            Part updated = partsService.update(part);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a part", description = "Deletes a part by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Part deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Part not found")
    })
    ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            partsService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}