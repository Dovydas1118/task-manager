package com.manager.task.task_manager_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.manager.task.task_manager_api.model.Project;
import com.manager.task.task_manager_api.service.ProjectService;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {
    @Autowired 
    private MockMvc mockMvc;

    @MockitoBean 
    private ProjectService projectService;

    @Test
    void getAllProjectsReturnsOk() throws Exception {
        Project project = new Project();
        project.setProjectName("Task Manager");
        project.setLead("Alice");

        List<Project> projects = List.of(project);


        when(projectService.getAllProjects()).thenReturn(projects);

        mockMvc.perform(get("/projects"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].projectName")
                .value("Task Manager"))
            .andExpect(jsonPath("$[0].lead")
                .value("Alice"));
    }

    @Test 
    void getProjectWithIdReturnsOk() throws Exception {
        Project project = new Project();
        project.setProjectName("Task Manager");
        project.setLead("Alice");

        when(projectService.getProject(42L)).thenReturn(project);

        mockMvc.perform(get("/projects/{id}", 42L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.projectName")
                .value("Task Manager"));

    }

    @Test 
    void createProjectReturnsCreated() throws Exception {
        Project project = new Project();
        project.setProjectName("Task Manager");
        project.setLead("Alice");

        when(projectService.createProject(any(Project.class))).thenReturn(project);

        mockMvc.perform(post("/projects")
            .content("""
                {
                    "projectName": "Task Manager",
                    "lead": "Alice"
                }
                """)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.projectName").value("Task Manager"));
    }

    @Test 
    void getProjectReturnsNotFound() throws Exception {
        when(projectService.getProject(999L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/projects/{id}",  999L))
            .andExpect(status().isNotFound());
    }
}