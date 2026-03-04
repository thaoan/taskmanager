package com.thaoan.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record Authentication(
    @Schema(example = "teste@teste.com", description = "E-mail do usuário cadastrado")
    @NotBlank
    String email,

    @Schema(example = "123", description = "Senha do usuário")
    @NotBlank
    String password
) {}