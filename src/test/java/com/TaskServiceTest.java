package com;

import com.thaoan.taskmanager.dto.TaskRequest;
import com.thaoan.taskmanager.exception.ResourceNotFoundException;
import com.thaoan.taskmanager.models.Task;
import com.thaoan.taskmanager.repository.TaskRepository;
import com.thaoan.taskmanager.service.TaskService;

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

    @InjectMocks
    private TaskService service;

    @Test
    @DisplayName("Deve atualizar uma tarefa com sucesso quando o ID existir")
    void atualizarSucesso() {
        // Arrange (Configuração)
        Long id = 1L;
        Task taskExistente = new Task();
        taskExistente.setId(id);
        taskExistente.setTitle("Antigo");

        TaskRequest request = new TaskRequest("Novo Titulo", "Desc", false);
        
        when(repository.findById(id)).thenReturn(Optional.of(taskExistente));
        when(repository.save(any(Task.class))).thenReturn(taskExistente);

        // Act (Execução)
        Task resultado = service.atualizar(id, request);

        // Assert (Verificação)
        assertNotNull(resultado);
        assertEquals("Novo Titulo", resultado.getTitle());
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando ID não existir")
    void atualizarErroIdInexistente() {
        // Arrange
        Long id = 99L;
        TaskRequest request = new TaskRequest("Titulo", "Desc", false);
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            service.atualizar(id, request);
        });

        verify(repository, never()).save(any(Task.class));
    }
}