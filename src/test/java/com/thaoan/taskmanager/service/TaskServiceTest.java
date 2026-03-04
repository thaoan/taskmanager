package com.thaoan.taskmanager.service;

import com.thaoan.taskmanager.dto.TaskRequest;
import com.thaoan.taskmanager.models.Category;
import com.thaoan.taskmanager.models.Task;
import com.thaoan.taskmanager.models.User; // Import novo
import com.thaoan.taskmanager.repository.CategoryRepository;
import com.thaoan.taskmanager.repository.TaskRepository;
import com.thaoan.taskmanager.repository.UserRepository; // Import novo
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
    @DisplayName("Deve salvar uma tarefa vinculada a uma categoria e um usuário")
    void salvarComSucesso() {
        // Arrange
        // Atualizado com 5 parâmetros: title, description, completed, categoryId, userId
        TaskRequest request = new TaskRequest("Teste", "Desc", false, 1L, 1L);
        
        Category categoria = new Category();
        categoria.setId(1L);

        User usuario = new User();
        usuario.setId(1L);
        
        // Configuramos os mocks para retornar os objetos quando buscados por ID
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario)); // Mock do usuário
        when(repository.save(any(Task.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Task resultado = service.salvar(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getCategory().getId());
        assertEquals(1L, resultado.getUser().getId()); // Verifica se o usuário foi vinculado
        verify(repository).save(any(Task.class));
    }
}