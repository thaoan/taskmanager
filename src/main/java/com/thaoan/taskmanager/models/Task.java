package com.thaoan.taskmanager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data 
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Garante que o título não seja nulo nem vazio no banco e na API
    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    @Column(nullable = false)
    private String title;

    private String description;

    private boolean completed = false;

    // updatable = false garante que a data de criação nunca mude após o insert
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Método que o Hibernate chama automaticamente antes de salvar no banco
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}