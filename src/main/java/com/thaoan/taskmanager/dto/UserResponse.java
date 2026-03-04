package com.thaoan.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
    @Schema(example = "1")
    Long id,

    @Schema(example = "thaoan")
    String username,

    @Schema(example = "thaoan@teste.com")
    String email,

    @Schema(example = "USER", description = "Papel do usuário no sistema (USER ou ADMIN)")
    String role
) {}