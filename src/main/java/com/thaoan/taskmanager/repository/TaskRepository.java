package com.thaoan.taskmanager.repository;

import com.thaoan.taskmanager.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // O Spring Data JPA gera a query automaticamente baseada no nome do método!
    List<Task> findByCompleted(boolean completed);
    
}