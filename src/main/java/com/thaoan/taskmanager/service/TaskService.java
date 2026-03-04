package com.thaoan.taskmanager.service;

import com.thaoan.taskmanager.dto.TaskRequest;
import com.thaoan.taskmanager.exception.ResourceNotFoundException;
import com.thaoan.taskmanager.models.Category;
import com.thaoan.taskmanager.models.Task;
import com.thaoan.taskmanager.repository.CategoryRepository;
import com.thaoan.taskmanager.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    private final TaskRepository repository;
    private final CategoryRepository categoryRepository;

    public TaskService(TaskRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

public Page<Task> listarComFiltros(Long categoryId, Boolean completed, String title, Pageable pageable) {
    // Filtro Triplo
    if (categoryId != null && completed != null && title != null && !title.isBlank()) {
        return repository.findByCategoryIdAndCompletedAndTitleContainingIgnoreCase(categoryId, completed, title, pageable);
    }
    
    // Se não cair no triplo, tenta as combinações duplas ou simples
    if (categoryId != null && completed != null) {
        return repository.findByCategoryIdAndCompleted(categoryId, completed, pageable);
    }
    
    if (title != null && !title.isBlank()) {
        return repository.findByTitleContainingIgnoreCase(title, pageable);
    }

    if (categoryId != null) {
        return repository.findByCategoryId(categoryId, pageable);
    }

    if (completed != null) {
        return repository.findByCompleted(completed, pageable);
    }

    return repository.findAll(pageable);
}

    @Transactional
    public Task salvar(TaskRequest request) {
        Task task = new Task();
        mapRequestToEntity(task, request);
        return repository.save(task);
    }

    @Transactional
    public Task atualizar(Long id, TaskRequest details) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com ID: " + id));

        mapRequestToEntity(task, details);
        return repository.save(task);
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Não é possível deletar: Tarefa inexistente com ID: " + id);
        }
        repository.deleteById(id);
    }

    private void mapRequestToEntity(Task task, TaskRequest request) {
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setCompleted(request.completed());

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + request.categoryId()));
            task.setCategory(category);
        }
    }
}