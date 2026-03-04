package com.thaoan.taskmanager.controller;

import com.thaoan.taskmanager.dto.CategoryRequest;
import com.thaoan.taskmanager.models.Category;
import com.thaoan.taskmanager.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Endpoints for managing task categories")
public class CategoryController {

    private final CategoryRepository repository;

    public CategoryController(CategoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Returns a list of all available categories for tasks.")
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

@PostMapping
@Operation(summary = "Create a new category", description = "Registers a new category to group tasks.")
public ResponseEntity<Category> create(@Valid @RequestBody CategoryRequest request) {
    Category category = new Category();
    category.setName(request.name());
    
    return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(category));
}
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category", description = "Removes a category by its ID.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    @Operation(summary = "Update a category", description = "Changes the name of an existing category.")
    public ResponseEntity<Category> update(@PathVariable Long id, @Valid @RequestBody Category category) {
        // Lógica simples: busca a antiga, muda o nome e salva
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(category.getName());
                    return ResponseEntity.ok(repository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}