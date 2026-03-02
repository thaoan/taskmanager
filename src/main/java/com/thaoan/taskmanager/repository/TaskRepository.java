package com.thaoan.taskmanager.repository;

import com.thaoan.taskmanager.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Aqui o Spring Data JPA já nos dá save(), findAll(), findById(), delete() de graça!
}