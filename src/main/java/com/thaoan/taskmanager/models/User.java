package com.thaoan.taskmanager.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Collection;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails { // Adicionado o implements
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Task> tasks;

    // --- MÉTODOS DO USERDETAILS (Obrigatórios para o Spring Security) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Por enquanto, todos os usuários terão a permissão "USER"
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        // No Spring Security, o "username" é o login único. 
        // Vamos usar o EMAIL como nosso login.
        return email; 
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}