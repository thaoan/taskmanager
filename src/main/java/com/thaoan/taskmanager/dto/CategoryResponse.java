package com.thaoan.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponse(
    @Schema(example = "1")
    Long id,

    @Schema(example = "Trabalho", description = "Nome da categoria")
    String name,

    @Schema(example = "Tarefas relacionadas ao escritório")
    String description
) {}