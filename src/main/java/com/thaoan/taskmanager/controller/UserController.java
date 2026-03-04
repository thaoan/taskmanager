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
    // O método de criação de usuário é público, pois é necessário para o registro. Os outros métodos são protegidos por autenticação.
    @PostMapping
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns public data.")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
      
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request));
    }
    // Metodo de listagem de usuários 
    @GetMapping
    @Operation(summary = "List all users", description = "Returns a list of all registered users without sensitive information.")
    public ResponseEntity<List<UserResponse>> list() {
        
        return ResponseEntity.ok(service.findAll());
    }
// metodo update e delete, ambos protegidos por autenticação, para garantir que apenas usuários autorizados possam modificar ou remover contas.
    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Updates user details like username or password based on the ID.")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Permanently removes a user from the system.")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}