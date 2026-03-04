package com.thaoan.taskmanager.service;

import com.thaoan.taskmanager.models.User;
import com.thaoan.taskmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List; 

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User salvar(User user) {
        // Criptografa a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    //metodo para listar todos os usuários (útil para testes e administração)
    public List<User> listarTodos() {
        return repository.findAll();
    }
}