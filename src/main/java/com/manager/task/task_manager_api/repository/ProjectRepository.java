package com.manager.task.task_manager_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.manager.task.task_manager_api.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    
}
