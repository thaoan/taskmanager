package com.thaoan.taskmanager.controller;

import com.thaoan.taskmanager.dto.UserRequest; 
import com.thaoan.taskmanager.dto.UserResponse;
import com.thaoan.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for managing system users")
public class UserController {

    private final UserService service; 

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns public data.")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
        // Corrigido de 'salvar' para 'save'
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }

    @GetMapping
    @Operation(summary = "List all users", description = "Returns a list of all registered users without sensitive information.")
    public ResponseEntity<List<UserResponse>> list() {
        // Corrigido de 'listarTodos' para 'findAll'
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Updates user details like username or password based on the ID.")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        // Corrigido de 'atualizar' para 'update'
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Permanently removes a user from the system.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Corrigido de 'deletar' para 'delete'
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}