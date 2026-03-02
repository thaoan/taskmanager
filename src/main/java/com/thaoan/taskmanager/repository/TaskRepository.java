package com.thaoan.taskmanager.repository;

import com.thaoan.taskmanager.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Agora o Spring gerencia a query com limites (LIMIT/OFFSET) automaticamente
    Page<Task> findByCompleted(boolean completed, Pageable pageable);
    
}