package com.thaoan.taskmanager.service;

import com.thaoan.taskmanager.dto.UserResponse; // Import do DTO
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

    // Agora retorna UserResponse (sem senha)
    public UserResponse salvar(User user) {
        // Criptografa a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userSalvo = repository.save(user);
        
        // Converte a Entity para o DTO de resposta
        return new UserResponse(userSalvo.getId(), userSalvo.getUsername(), userSalvo.getEmail());
    }

    // Retorna uma lista de DTOs limpos
    public List<UserResponse> listarTodos() {
        return repository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }
}