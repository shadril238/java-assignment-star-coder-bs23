package com.shadril.taskmanagementjava.repository;

import com.shadril.taskmanagementjava.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.id = :taskId AND t.isDeleted = false")
    Optional <Task> findByIdAndIsDeletedFalse(@Param("taskId") Long taskId);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.isDeleted = false")
    List <Task> findAllByUserIdAndIsDeletedFalse(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.isCompleted = true AND t.isDeleted = false")
    List <Task> findAllByUserIdAndIsCompletedTrueAndIsDeletedFalse(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.isCompleted = false AND t.isDeleted = false")
    List <Task> findAllByUserIdAndIsCompletedFalseAndIsDeletedFalse(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.isDeleted = false ORDER BY t.createdAt ASC")
    List <Task> findAllByUserIdAndIsDeletedFalseOrderByCreatedAtAsc(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.isDeleted = false ORDER BY t.createdAt DESC")
    List <Task> findAllByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(@Param("userId") Long userId);

    List<Task> findAllByIsDeletedFalse();


}
