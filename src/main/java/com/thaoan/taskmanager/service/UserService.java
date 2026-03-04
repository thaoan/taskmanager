package com.thaoan.taskmanager.service;

import com.thaoan.taskmanager.dto.UserRequest;
import com.thaoan.taskmanager.dto.UserResponse;
import com.thaoan.taskmanager.models.User;
import com.thaoan.taskmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse save(UserRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        User savedUser = repository.save(user);
        
        return mapToResponse(savedUser);
    }

    public List<UserResponse> findAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // NOVO: Método para atualizar usuário
    public UserResponse update(Long id, UserRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setUsername(request.username());
        user.setEmail(request.email());
        // Apenas atualiza a senha se ela não estiver vazia
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        User updatedUser = repository.save(user);
        return mapToResponse(updatedUser);
    }

    // NOVO: Método para deletar usuário
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // Método auxiliar para evitar repetição de código (DRY)
    private UserResponse mapToResponse(User user) {
        return new UserResponse(
            user.getId(), 
            user.getUsername(), 
            user.getEmail(),
            "USER" 
        );
    }
}