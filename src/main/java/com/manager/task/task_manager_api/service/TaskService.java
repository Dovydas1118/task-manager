package com.manager.task.task_manager_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.manager.task.task_manager_api.model.Project;
import com.manager.task.task_manager_api.model.Task;
import com.manager.task.task_manager_api.repository.ProjectRepository;
import com.manager.task.task_manager_api.repository.TaskRepository;

@Service 
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public Task getTaskById(Long projectId, Long taskId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<Task> optionalTask = taskRepository.findByIdAndProjectId(taskId, projectId);
        if (optionalTask.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return optionalTask.get();
    }

    public List<Task> getTasks(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Project project = optionalProject.get();

        return project.getTasks();
    }

    public Task createTask(Long projectId, Task task) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Project project = optionalProject.get();
        project.addTask(task);
    
        return taskRepository.save(task);
    }

    
}
