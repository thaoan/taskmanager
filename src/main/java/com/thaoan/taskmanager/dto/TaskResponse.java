package com.thaoan.taskmanager.dto;

public record TaskResponse(
    Long id,
    String title,
    String description,
    boolean completed,
    CategoryResponse category, // Se você tiver um CategoryResponse, senão pode usar a Entity por enquanto
    UserResponse user
) {}