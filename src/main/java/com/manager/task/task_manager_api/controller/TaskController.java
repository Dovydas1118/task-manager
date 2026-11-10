package com.manager.task.task_manager_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.manager.task.task_manager_api.model.Task;
import com.manager.task.task_manager_api.service.TaskService;

@RestController 
public class TaskController {
    
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping("/projects/{id}/tasks")
    public ResponseEntity<Task> createTask(@PathVariable Long id, @RequestBody Task task) {
        Task savedTask = service.createTask(id, task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @GetMapping("/projects/{id}/tasks")
    public ResponseEntity<List<Task>> getAllTasksInProject(@PathVariable Long id) {
        List<Task> task = service.getTasks(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long projectId, @PathVariable Long taskId) {
        Task task = service.getTaskById(projectId, taskId);
        return ResponseEntity.ok(task);
    }
}
