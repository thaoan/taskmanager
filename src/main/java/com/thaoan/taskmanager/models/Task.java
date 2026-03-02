package com.thaoan.taskmanager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data 
@EntityListeners(AuditingEntityListener.class) // <--- OBRIGATÓRIO para o Spring "ouvir" as datas
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    @Column(nullable = false)
    private String title;

    private String description;

    private boolean completed = false;

    @CreatedDate // Substitui o seu @PrePersist e o LocalDateTime.now() manual
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // NOVO: Registra automaticamente quando você editar a tarefa
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}