package com.manager.task.task_manager_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.manager.task.task_manager_api.model.Category;
import com.manager.task.task_manager_api.model.Project;
import com.manager.task.task_manager_api.model.Task;
import com.manager.task.task_manager_api.service.TaskService;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired 
    private MockMvc mockMvc;
   
    @MockitoBean 
    private TaskService taskService;

    private Project project;

    @BeforeEach 
    void setUp() {
        project = new Project();
        project.setProjectName("Task Manager");
        project.setLead("Alice");
    }

    @Test
    void createTaskReturnsCreated() throws Exception {
        Task task = new Task();
        task.setProject(project);
        task.setTaskName("Task 1");
        task.setCategory(Category.TESTING);
        when(taskService.createTask(eq(1L), any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/projects/{id}/tasks", 1L)
            .content("""
            {
                "taskName": "Task 1",
                "category": "TESTING"
            }    
            """)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.taskName").value("Task 1"));
    }

    @Test
    void createTaskReturnsNotFound() throws Exception {
        Task task = new Task();
        task.setTaskName("Task with no project");
        when(taskService.createTask(eq(999L), any(Task.class))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        mockMvc.perform(post("/projects/{id}/tasks", 999L)
            .content("""
                {
                    "taskName": "Task with no project",
                    "category": "TESTING"
                } 
                """)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void getAllTasksInProjectReturnsOk() throws Exception {
        Task task1 = new Task();
        task1.setTaskName("Task 1");
        task1.setCategory(Category.TESTING);
        task1.setProject(project);

        Task task2 = new Task();
        task2.setTaskName("Task 2");
        task2.setCategory(Category.DESIGN);
        task2.setProject(project);

        List<Task> tasks = new ArrayList<Task>();
        tasks.add(task1);
        tasks.add(task2);

        when(taskService.getTasks(1L)).thenReturn(tasks);

        mockMvc.perform(get("/projects/{id}/tasks", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].taskName")
                .value("Task 1"))
            .andExpect(jsonPath("$[1].taskName")
                .value("Task 2"));
        
    }

    @Test 
    void getAllTasksInProjectReturnsNotFound() throws Exception {
        when(taskService.getTasks(999L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/projects/{id}/tasks", 999L))
            .andExpect(status().isNotFound());
    }

    @Test 
    void getTaskByIdReturnsOk() throws Exception {
        Task task = new Task();
        task.setTaskName("Task 1");
        task.setCategory(Category.TESTING);
        task.setProject(project);

        when(taskService.getTaskById(1L, 1L)).thenReturn(task);

        mockMvc.perform(get("/projects/{projectId}/tasks/{taskId}", 1L, 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.taskName")
                .value("Task 1"));

    }

    @Test 
    void getTaskByIdReturnsNotFound() throws Exception {
        when(taskService.getTaskById(1L, 999L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/projects/{projectId}/tasks/{taskId}", 1L, 999L))
            .andExpect(status().isNotFound());

    }
}
