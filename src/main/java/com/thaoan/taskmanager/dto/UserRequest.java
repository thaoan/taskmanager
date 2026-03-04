package com.thaoan.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
    @Schema(example = "thaoan")
    @NotBlank(message = "O nome de usuário é obrigatório")
    String username,

    @Schema(example = "thaoan@teste.com")
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    String email,

    @Schema(example = "123")
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 3, message = "A senha deve ter no mínimo 3 caracteres")
    String password
) {}