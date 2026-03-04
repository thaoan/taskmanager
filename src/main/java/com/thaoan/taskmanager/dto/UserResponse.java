package com.thaoan.taskmanager.dto;

import com.thaoan.taskmanager.models.User;

public record UserResponse(
    Long id,
    String username,
    String email
) {
    // Método estático para converter a Entity User para o DTO UserResponse 
    public static UserResponse fromEntity(User user) {
        if (user == null) return null;
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }
}