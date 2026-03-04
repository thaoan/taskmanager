package com.thaoan.taskmanager.repository;

import com.thaoan.taskmanager.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // 1. O FILTRO TOTAL: Busca do usuário logado por Título, Categoria e Status
    Page<Task> findByUserIdAndCategoryIdAndCompletedAndTitleContainingIgnoreCase(
            Long userId, Long categoryId, Boolean completed, String title, Pageable pageable);

    // 2. Busca por usuário e título
    Page<Task> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title, Pageable pageable);

    // 3. Filtra por usuário e status
    Page<Task> findByUserIdAndCompleted(Long userId, Boolean completed, Pageable pageable);

    // 4. Filtra por usuário e categoria
    Page<Task> findByUserIdAndCategoryId(Long userId, Long categoryId, Pageable pageable);

    // 5. Filtra por usuário, categoria e status
    Page<Task> findByUserIdAndCategoryIdAndCompleted(Long userId, Long categoryId, Boolean completed, Pageable pageable);
    
    // 6. Lista tudo de um usuário específico
    Page<Task> findByUserId(Long userId, Pageable pageable);
}