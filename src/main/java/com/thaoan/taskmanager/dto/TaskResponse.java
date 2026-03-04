package com.thaoan.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record TaskResponse(
    @Schema(example = "1")
    Long id,

    @Schema(example = "Finalizar Projeto API")
    String title,

    @Schema(example = "Configurar Swagger e JWT")
    String description,

    @Schema(example = "false")
    boolean completed,

    @Schema(example = "2026-12-31T23:59:59")
    LocalDateTime createdAt,

    @Schema(description = "Dados resumidos da categoria")
    CategoryResponse category,

    @Schema(example = "thaoan", description = "Username do dono da tarefa")
    String ownerName
) {}