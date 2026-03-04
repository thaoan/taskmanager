package com.thaoan.taskmanager.repository;

import com.thaoan.taskmanager.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Busca por título (palavra-chave)
    Page<Task> findByTitleContainingIgnoreCase(String title, Pageable pageable);
   
    // Busca por categoria, status E título ao mesmo tempo
    Page<Task> findByCategoryIdAndCompletedAndTitleContainingIgnoreCase(Long categoryId, Boolean completed, String title, Pageable pageable);
    
    // Filtra por status
    Page<Task> findByCompleted(Boolean completed, Pageable pageable);

    // Filtra por categoria
    Page<Task> findByCategoryId(Long categoryId, Pageable pageable);

    // Filtra por ambos ao mesmo tempo 
    Page<Task> findByCategoryIdAndCompleted(Long categoryId, Boolean completed, Pageable pageable);
}