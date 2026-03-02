package com.thaoan.taskmanager.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data // O Lombok gera getters e setters automaticamente
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private boolean completed = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}