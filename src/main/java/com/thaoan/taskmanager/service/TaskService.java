package com.thaoan.taskmanager.service;

import com.thaoan.taskmanager.dto.TaskRequest;
import com.thaoan.taskmanager.exception.ResourceNotFoundException;
import com.thaoan.taskmanager.models.Category;
import com.thaoan.taskmanager.models.Task;
import com.thaoan.taskmanager.models.User; // Import novo
import com.thaoan.taskmanager.repository.CategoryRepository;
import com.thaoan.taskmanager.repository.TaskRepository;
import com.thaoan.taskmanager.repository.UserRepository; // Import novo
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    private final TaskRepository repository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository; // Injeção nova

    // Construtor atualizado com as 3 dependências
    public TaskService(TaskRepository repository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Page<Task> listarComFiltros(Long categoryId, Boolean completed, String title, Pageable pageable) {
        if (categoryId != null && completed != null && title != null && !title.isBlank()) {
            return repository.findByCategoryIdAndCompletedAndTitleContainingIgnoreCase(categoryId, completed, title, pageable);
        }
        
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

        // Busca e associa a Categoria
        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + request.categoryId()));
            task.setCategory(category);
        }

        // Busca e associa o Usuário (Dono da tarefa)
        if (request.userId() != null) {
            User user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + request.userId()));
            task.setUser(user);
        }
    }
}