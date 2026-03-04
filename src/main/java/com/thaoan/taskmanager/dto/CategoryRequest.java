package com.thaoan.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
    @Schema(example = "Work")
    @NotBlank(message = "Category name is required")
    String name
) {}