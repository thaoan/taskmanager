package com.thaoan.taskmanager.controller;

import com.thaoan.taskmanager.dto.Authentication;
import com.thaoan.taskmanager.dto.LoginResponse;
import com.thaoan.taskmanager.models.User;
import com.thaoan.taskmanager.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user login and token management")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    @Operation(
        summary = "User Login", 
        description = "Authenticates user credentials and returns a Bearer Token (JWT) for accessing protected routes."
    )
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid Authentication data) {
        // Create authentication token
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        
        // Authenticate the user
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // Generate the JWT Token
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponse(token));
    }
}