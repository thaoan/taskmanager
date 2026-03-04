package com.thaoan.taskmanager.service; // Verifique se o package bate com a pasta

import com.thaoan.taskmanager.dto.TaskRequest;
import com.thaoan.taskmanager.models.Category;
import com.thaoan.taskmanager.models.Task;
import com.thaoan.taskmanager.repository.CategoryRepository;
import com.thaoan.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TaskService service;

    @Test
    @DisplayName("Deve salvar uma tarefa vinculada a uma categoria")
    void salvarComSucesso() {
        // Arrange
        TaskRequest request = new TaskRequest("Teste", "Desc", false, 1L);
        Category categoria = new Category();
        categoria.setId(1L);
        
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(repository.save(any(Task.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Task resultado = service.salvar(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getCategory().getId());
        verify(repository).save(any(Task.class));
    }
}