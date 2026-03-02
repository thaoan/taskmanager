package com.thaoan.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(
    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    String title,
    @NotBlank(message = "A descrição é obrigatória") // A descrição é obrigatória, mas pode ser vazia
    String description,

    boolean completed
) {}