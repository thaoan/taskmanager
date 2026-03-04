package com.thaoan.taskmanager.controller;

import com.thaoan.taskmanager.dto.UserResponse;
import com.thaoan.taskmanager.models.User;
import com.thaoan.taskmanager.service.UserService; // Import do Service

import org.springframework.http.HttpStatus;
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
public ResponseEntity<UserResponse> create(@RequestBody User user) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(user));
}

@GetMapping
public ResponseEntity<List<UserResponse>> listar() {
    return ResponseEntity.ok(service.listarTodos());
}
}