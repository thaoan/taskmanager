package com.thaoan.taskmanager.service;

import com.thaoan.taskmanager.dto.TaskRequest;
import com.thaoan.taskmanager.dto.TaskResponse; 
import com.thaoan.taskmanager.models.Category;
import com.thaoan.taskmanager.models.Task;
import com.thaoan.taskmanager.models.User;
import com.thaoan.taskmanager.repository.CategoryRepository;
import com.thaoan.taskmanager.repository.TaskRepository;
import com.thaoan.taskmanager.repository.UserRepository;
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

    @Mock
    private UserRepository userRepository; 

    @InjectMocks
    private TaskService service;

    @Test
    @DisplayName("Deve salvar uma tarefa vinculada a uma categoria e um usuário e retornar TaskResponse")
    void salvarComSucesso() {
        // Arrange
        TaskRequest request = new TaskRequest("Teste", "Desc", false, 1L, 1L);
        
        Category categoria = new Category();
        categoria.setId(1L);
        categoria.setName("Trabalho");

        User usuario = new User();
        usuario.setId(1L);
        usuario.setUsername("teste");
        usuario.setEmail("teste@teste.com");
        
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        
        // Mock do save: Quando salvar, ele retorna uma Task completa com os objetos dentro
        when(repository.save(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setId(10L); // Simula o ID gerado pelo banco
            return t;
        });

        // Act
        // O RETORNO AGORA É TaskResponse!
        TaskResponse resultado = service.salvar(request);

        // Assert
        assertNotNull(resultado);
        assertEquals("Teste", resultado.title());
        assertEquals(1L, resultado.category().id()); // Verifica o ID no DTO de categoria
        assertEquals(1L, resultado.user().id());     // Verifica o ID no DTO de usuário
        
        verify(repository).save(any(Task.class));
    }
}