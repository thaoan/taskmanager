package com.thaoan.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
        .authorizeHttpRequests(auth -> auth
            // Permite documentação do Swagger
            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            
            // Permite os recursos da API (Libere o que você quer testar sem senha)
            .requestMatchers("/api/tasks/**").permitAll()
            .requestMatchers("/api/categories/**").permitAll()
            .requestMatchers("/api/users/**").permitAll() // <--- ADICIONE ESTA LINHA AQUI!
            
            // O que não estiver acima, o Spring vai bloquear pedindo login
            .anyRequest().authenticated()
        );
    return http.build();
}
}