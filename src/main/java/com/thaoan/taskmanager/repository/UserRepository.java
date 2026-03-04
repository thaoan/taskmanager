package com.thaoan.taskmanager.repository;

import com.thaoan.taskmanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Método para buscar usuário pelo nome (útil para o futuro login)
    Optional<User> findByUsername(String username);
    
    // Método para verificar se um e-mail já está cadastrado
    Optional<User> findByEmail(String email);
}