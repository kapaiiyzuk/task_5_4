package org.example.task_5_1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {
    @GetMapping({"/admin", "/admin/users"})
    public String adminPage() {
        return "admin";
    }
}
