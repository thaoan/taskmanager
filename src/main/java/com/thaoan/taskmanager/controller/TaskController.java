package com.thaoan.taskmanager.controller;

import com.thaoan.taskmanager.dto.TaskRequest;
import com.thaoan.taskmanager.dto.TaskResponse;
import com.thaoan.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Endpoints for managing user tasks and assignments")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List tasks with filters", description = "Returns a paginated list of tasks. You can filter by user, category, completion status, or title.")
    public ResponseEntity<Page<TaskResponse>> list(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        
        return ResponseEntity.ok(service.listarComFiltros(userId, categoryId, completed, title, pageable));
    }

    @PostMapping
    @Operation(summary = "Create a new task", description = "Saves a new task associated with a user and a category.")
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task", description = "Updates task details such as title, description, or status based on the provided ID.")
    public ResponseEntity<TaskResponse> update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        TaskResponse updatedTask = service.atualizar(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Permanently removes a task from the system.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}