package com.thaoan.taskmanager.controller;

import com.thaoan.taskmanager.models.Task;
import com.thaoan.taskmanager.service.TaskService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> getAll() {
        return service.listarTodas();
    }

    // Novo endpoint de filtro que criamos
    @GetMapping("/filter")
    public List<Task> filterByStatus(@RequestParam boolean completed) {
        return service.buscarPorStatus(completed);
    }

    @PostMapping
    public Task create(@Valid @RequestBody Task task) {
        return service.salvar(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        // O Service agora cuida da lógica de encontrar e atualizar
        return service.atualizar(id, taskDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}