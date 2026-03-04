package com.thaoan.taskmanager.repository;

import com.thaoan.taskmanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails; // Import novo
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    // O Spring Security usa este método para carregar o usuário durante a validação do Token
    UserDetails findByEmail(String email);

    // Mantemos este para validações extras se precisar (como no cadastro)
    boolean existsByEmail(String email);
}