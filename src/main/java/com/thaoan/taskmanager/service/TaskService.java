package com.thaoan.taskmanager.service;

import com.thaoan.taskmanager.models.Task;
import com.thaoan.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> listarTodas() {
        return repository.findAll();
    }

    public List<Task> buscarPorStatus(boolean completed) {
        return repository.findByCompleted(completed);
    }

    public Task salvar(Task task) {
        return repository.save(task);
    }

    // Método de atualização que o Controller agora espera
    public Optional<Task> atualizar(Long id, Task details) {
        return repository.findById(id).map(task -> {
            task.setTitle(details.getTitle());
            task.setDescription(details.getDescription());
            task.setCompleted(details.isCompleted());
            return repository.save(task);
        });
    }

    // Método de exclusão que retorna booleano para o Controller decidir o status HTTP
    public boolean deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}