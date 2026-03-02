package com.thaoan.taskmanager.controller;

import com.thaoan.taskmanager.models.Task;
import com.thaoan.taskmanager.repository.TaskRepository;
import jakarta.validation.Valid; // Importe necessário para a validação
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Task> getAll() {
        return repository.findAll();
    }

    @PostMapping
    // Adicionamos o @Valid aqui para ativar as regras da model
    public Task create(@Valid @RequestBody Task task) {
        return repository.save(task);
    }

    @PutMapping("/{id}")
    // Adicionamos o @Valid aqui também para garantir que a atualização siga as regras
    public ResponseEntity<Task> update(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        return repository.findById(id).map(task -> {
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setCompleted(taskDetails.isCompleted());
            return ResponseEntity.ok(repository.save(task));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}