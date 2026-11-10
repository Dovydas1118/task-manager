package com.manager.task.task_manager_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manager.task.task_manager_api.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndProjectId(Long taskId, Long projectId);
}
