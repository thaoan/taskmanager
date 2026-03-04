package com.thaoan.taskmanager.controller;

import com.thaoan.taskmanager.models.User;
import com.thaoan.taskmanager.service.UserService; // Import do Service
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service; 
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> cadastrar(@RequestBody User user) {
        
        return ResponseEntity.ok(service.salvar(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }
}