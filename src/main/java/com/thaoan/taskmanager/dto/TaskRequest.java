package com.thaoan.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequest(
    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    String title,

    @NotBlank(message = "A descrição é obrigatória")
    String description,

    boolean completed,

    @NotNull(message = "O ID da categoria é obrigatório")
    Long categoryId,

    @NotNull(message = "O ID do usuário é obrigatório") 
    Long userId
) {}