package com.manager.task.task_manager_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class TaskManagerController {
    @GetMapping("/api/status")
    public String home() {
        return "The api is working\n";
    }

    
}
