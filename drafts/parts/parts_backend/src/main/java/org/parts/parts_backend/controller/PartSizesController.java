package org.parts.parts_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.parts.parts_backend.entity.PartSize;
import org.parts.parts_backend.service.PartSizesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sizes")
@Tag(name = "Part sizes", description = "Part sizes management APIs")
class PartSizesController {
    private final PartSizesService service;

    PartSizesController(PartSizesService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all part sizes", description = "Retrieves a list of all part sizes")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of part sizes")
    List<PartSize> all() {
        return service.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a new part size", description = "Creates a new part size with the provided name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Part size created successfully",
                    content = @Content(schema = @Schema(implementation = PartSize.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    ResponseEntity<@NonNull PartSize> create(@RequestBody PartSize size) {
        service.insert(size);
        return ResponseEntity.status(HttpStatus.CREATED).body(size);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a PartSize", description = "Updates an existing part size by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Part size updated successfully",
                    content = @Content(schema = @Schema(implementation = PartSize.class))),
            @ApiResponse(responseCode = "404", description = "Part size not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    ResponseEntity<@NonNull PartSize> update(@PathVariable String id, @RequestBody PartSize size) {
        try {
            service.update(id, size.getId());
            return ResponseEntity.ok(size);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a PartSize", description = "Deletes a PartSize by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "PartSize deleted successfully"),
            @ApiResponse(responseCode = "404", description = "PartSize not found")
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
