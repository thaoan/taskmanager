package com.thaoan.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequest(
    @Schema(example = "Estudar Spring Security", description = "Título da tarefa")
    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    String title,

    @Schema(example = "Finalizar a configuração do Swagger e JWT", description = "Descrição da tarefa")
    @NotBlank(message = "A descrição é obrigatória")
    String description,

    @Schema(example = "false")
    boolean completed,

    @Schema(example = "1")
    @NotNull(message = "O ID da categoria é obrigatório")
    Long categoryId,

    @Schema(example = "1")
    @NotNull(message = "O ID do usuário é obrigatório") 
    Long userId
) {}