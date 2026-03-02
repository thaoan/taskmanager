package com.thaoan.taskmanager.service;

import com.thaoan.taskmanager.dto.TaskRequest;
import com.thaoan.taskmanager.exception.ResourceNotFoundException; // Importada!
import com.thaoan.taskmanager.models.Task;
import com.thaoan.taskmanager.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Page<Task> listarTodas(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Task> buscarPorStatus(boolean completed, Pageable pageable) {
        return repository.findByCompleted(completed, pageable);
    }

    @Transactional
    public Task salvar(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setCompleted(request.completed());
        return repository.save(task);
    }

    @Transactional
    public Task atualizar(Long id, TaskRequest details) {
        // Agora usando a exceção customizada para retornar 404
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com ID: " + id));

        task.setTitle(details.title());
        task.setDescription(details.description());
        task.setCompleted(details.completed());
        
        return repository.save(task);
    }

    @Transactional
    public void deletar(Long id) {
        // Verificação robusta antes de deletar
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Não é possível deletar: Tarefa inexistente com ID: " + id);
        }
        repository.deleteById(id);
    }
}