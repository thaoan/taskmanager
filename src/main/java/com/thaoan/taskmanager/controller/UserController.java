package com.thaoan.taskmanager.controller;

import com.thaoan.taskmanager.models.User;
import com.thaoan.taskmanager.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<User> cadastrar(@RequestBody User user) {
        return ResponseEntity.ok(repository.save(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }
}