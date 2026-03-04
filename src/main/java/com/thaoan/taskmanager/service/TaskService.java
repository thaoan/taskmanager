package com.thaoan.taskmanager.service;

import com.thaoan.taskmanager.dto.*;
import com.thaoan.taskmanager.exception.ResourceNotFoundException;
import com.thaoan.taskmanager.models.Category;
import com.thaoan.taskmanager.models.Task;
import com.thaoan.taskmanager.models.User;
import com.thaoan.taskmanager.repository.CategoryRepository;
import com.thaoan.taskmanager.repository.TaskRepository;
import com.thaoan.taskmanager.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    private final TaskRepository repository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository repository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Page<TaskResponse> listarComFiltros(Long userId, Long categoryId, Boolean completed, String title, Pageable pageable) {
        Page<Task> tasks;
        // ... sua lógica de filtros continua igual ...
        if (userId == null) {
            tasks = repository.findAll(pageable);
        } else if (categoryId != null && completed != null && title != null && !title.isBlank()) {
            tasks = repository.findByUserIdAndCategoryIdAndCompletedAndTitleContainingIgnoreCase(userId, categoryId, completed, title, pageable);
        } else if (categoryId != null && completed != null) {
            tasks = repository.findByUserIdAndCategoryIdAndCompleted(userId, categoryId, completed, pageable);
        } else if (title != null && !title.isBlank()) {
            tasks = repository.findByUserIdAndTitleContainingIgnoreCase(userId, title, pageable);
        } else if (categoryId != null) {
            tasks = repository.findByUserIdAndCategoryId(userId, categoryId, pageable);
        } else if (completed != null) {
            tasks = repository.findByUserIdAndCompleted(userId, completed, pageable);
        } else {
            tasks = repository.findByUserId(userId, pageable);
        }

        return tasks.map(this::convertToResponse);
    }

    @Transactional
    public TaskResponse salvar(TaskRequest request) {
        Task task = new Task();
        mapRequestToEntity(task, request);
        return convertToResponse(repository.save(task));
    }

    @Transactional
    public TaskResponse atualizar(Long id, TaskRequest details) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com ID: " + id));
        mapRequestToEntity(task, details);
        return convertToResponse(repository.save(task));
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
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
            task.setCategory(category);
        }

        if (request.userId() != null) {
            User user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
            task.setUser(user);
        }
    }

    private TaskResponse convertToResponse(Task task) {
        CategoryResponse categoryDTO = null;
        if (task.getCategory() != null) {
            // Ajustado para bater com: Long id, String name, String description
            categoryDTO = new CategoryResponse(
                task.getCategory().getId(),
                task.getCategory().getName(),
                "" // Se sua entity Category não tiver descrição, mande vazio ou null
            );
        }

        // Ajustado para bater com o novo TaskResponse:
        // Long id, String title, String description, boolean completed, LocalDateTime createdAt, CategoryResponse category, String ownerName
        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.isCompleted(),
            task.getCreatedAt(), // Certifique-se que sua Entity Task tem esse campo
            categoryDTO,
            task.getUser() != null ? task.getUser().getUsername() : "Sem dono"
        );
    }
}